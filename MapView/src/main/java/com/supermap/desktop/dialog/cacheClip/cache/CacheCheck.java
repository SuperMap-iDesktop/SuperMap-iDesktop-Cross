package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.*;
import com.supermap.data.processing.CacheWriter;
import com.supermap.data.processing.CompactFile;
import com.supermap.data.processing.StorageType;
import com.supermap.tilestorage.TileContent;
import com.supermap.tilestorage.TileStorageConnection;
import com.supermap.tilestorage.TileStorageManager;
import com.supermap.tilestorage.TileStorageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CacheCheck {

    /**
     * 检测缓存是否正确类
     *
     * @param args
     */
    public static void main(String[] args) {

        if (args.length < 3 || args[0].equalsIgnoreCase("--help")) {
            System.out.println("CacheCheck arguments instructions:");
            System.out.println("cacheroot scipath processcount mergetaskcount mongoconninfo");
            return;
        }

        String cacheRoot = args[0];
        String scipath = args[1];
        int processcount = Integer.valueOf(args[2]);
        int mergeTaskCount = 0;
        if (args.length > 3) {
            mergeTaskCount = Integer.valueOf(args[3]);
        }
        boolean error2udb = false;
        if (args.length > 4) {
            error2udb = Boolean.valueOf(args[4]);
        }
        Geometry boundaryRegion = null;
        if (args.length > 5) {
            boundaryRegion = Toolkit.GeoJsonToGemetry(args[5]);
        }

        ArrayList<String> sciNames = getSciFileList(scipath);

        if (sciNames.size() == 0) {
            System.out.println("sci file not found!");
            return;
        }
        if (error2udb) {
            File scifile = new File(sciNames.get(0));
            String datasourcePath = scifile.getParentFile().getParent() + "/udb/cache.udb";
            datasourcePath = datasourcePath.replaceAll("\\\\", "/");
            File datasourceFile = new File(datasourcePath);
            if (!datasourceFile.exists()) {
                System.out.println(datasourcePath + " not exists!");
                return;
            }
        }

        if (processcount >= 0) {
            LogWriter.setWriteToFile(true);
        }

        CacheCheck cacheCheck = new CacheCheck();
        cacheCheck.initalize(cacheRoot, sciNames, processcount, mergeTaskCount, error2udb, boundaryRegion);

        // 当多任务合并一起完成后，需要再单一再执行剩余的任务
        if (processcount > 0 && mergeTaskCount > 1) {
            String tempPath = scipath;
            if (scipath.endsWith(".list")) {
                // 这时传文件夹去列出剩余的文件再检查
                File tempFile = new File(scipath);
                tempPath = tempFile.getParent();
            }
            sciNames = getSciFileList(scipath);
            if (sciNames.size() > 0) {
                cacheCheck.initalize(cacheRoot, sciNames, processcount, 0, error2udb, boundaryRegion);
            }
        }
    }

    private static int sciIndex = 0;
    private String m_sciFilePath = "";
    private String m_errorFileName;
    private BufferedWriter m_errorWriter = null;
    private boolean m_error2udb = false;
    private boolean m_boundaryCheck = false;
    private Geometry m_boundaryRegion = null;

    public void initalize(String cacheRoot, ArrayList<String> sciFiles, int processcount, int mergeTaskCount, boolean error2udb, Geometry boundaryRegion) {
        m_error2udb = error2udb;
        m_boundaryRegion = boundaryRegion;
        m_boundaryCheck = m_boundaryRegion != null;
        CacheWriter writer = new CacheWriter();
        writer.FromConfigFile(sciFiles.get(0));
        double anchorLeft = writer.getIndexBounds().getLeft();
        double anchorTop = writer.getIndexBounds().getTop();
        int tileSize = writer.getTileSize().value();

        if (processcount > 0) {

            final ArrayList<String> argsSciFiles = sciFiles;
            final String argsCacheRoot = cacheRoot;
            final int argsMergeTaskCount = mergeTaskCount;
            final String argsError2udb = String.valueOf(error2udb);
            sciIndex = 0;

            MultiProcessManager processManager = new MultiProcessManager(this, processcount);
            processManager.setTimeout(2 * 60 * 1000);
            processManager.addProcessBuildingListener(new ProcessBuildingListener() {
                @Override
                public void ProcessBuilding(ProcessBuildEvent event) {
                    if (sciIndex < argsSciFiles.size()) {
                        StringBuilder strBuilder = new StringBuilder();
                        strBuilder.append(argsSciFiles.get(sciIndex));
                        sciIndex++;
                        if (argsMergeTaskCount > 1) {
                            for (int i = 1; i < argsMergeTaskCount; i++) {
                                if (sciIndex >= argsSciFiles.size()) {
                                    break;
                                }
                                strBuilder.append("&");
                                strBuilder.append(argsSciFiles.get(sciIndex));
                                sciIndex++;
                            }
                        }
                        String sciname = strBuilder.toString();

                        ArrayList<String> argsList = new ArrayList<String>();
                        argsList.add(argsCacheRoot);
                        argsList.add(sciname);
                        argsList.add("-1");
                        argsList.add("0");
                        argsList.add(argsError2udb);
                        event.setActive(true);
                        event.setArgs(argsList);
                    }
                }
            });

            processManager.Run();

            if (m_error2udb) {
                this.Error2Udb(anchorLeft, anchorTop, tileSize, sciFiles.get(0));
            }
        } else {
            String parentName = null;
            if (sciFiles.size() > 0) {
                parentName = new File(sciFiles.get(0)).getParentFile().getParent();
            }
            for (String scifile : sciFiles) {
                check(cacheRoot, scifile);
            }

            if (m_error2udb && processcount == 0 && null != parentName) {
                this.Error2Udb(anchorLeft, anchorTop, tileSize, parentName);
            }
        }
    }

    public void check(String cacheRoot, String sciFile) {
        File file = new File(sciFile);
        if (!file.exists()) {
            return;
        }
        m_sciFilePath = file.getName();
        m_errorWriter = null;
//		MapCacheFile cacheFile = new MapCacheFile();
//		cacheFile.open(sciFile);
        CacheWriter cacheFile = new CacheWriter();
        cacheFile.FromConfigFile(sciFile);

        cacheRoot = cacheRoot + "/" +
                cacheFile.parseTileFormat() + "_" +
                cacheFile.getTileSize().value() + "_" +
                cacheFile.getHashCode();

        TileStorageManager manager = null;
        if (cacheFile.getStorageType() == StorageType.MongoDB) {
            String[] strInfo = cacheFile.getMongoConnectionInfo();
            if (strInfo == null || strInfo.length < 3) {
                LogWriter.getInstance().writelog("error: mongo connection info!");
                return;
            }
            String server = strInfo[0];
            String database = strInfo[1];
            String cacheName = strInfo[2];

            TileStorageConnection connection = new TileStorageConnection();
            connection.setStorageType(TileStorageType.MONGOV2);
            connection.setServer(server);
            connection.setDatabase(database);
            connection.setName(cacheName);

            manager = new TileStorageManager();
            if (!manager.open(connection)) {
                LogWriter.getInstance().writelog("error: mongo open failed!");
                return;
            }
        }

        boolean result = true;
        for (Double scale : cacheFile.getCacheScaleCaptions().keySet()) {
            String caption = cacheFile.getCacheScaleCaptions().get(scale);
            String errFileName = file.getName().replaceAll(".sci", "");
            errFileName += "(L" + caption + "_S" + Math.round(1 / scale) + ").err";
            m_errorFileName = file.getParentFile().getParent() + "/temp/" + errFileName;

            double reolustion = getReolustion(scale, cacheFile.getPrjCoordSys(), cacheFile.getDPI());

            double left = ((cacheFile.getCacheBounds().getLeft() - cacheFile.getIndexBounds().getLeft()) / reolustion) / cacheFile.getTileSize().value();
            double top = ((cacheFile.getIndexBounds().getTop() - cacheFile.getCacheBounds().getTop()) / reolustion) / cacheFile.getTileSize().value();
            double right = ((cacheFile.getCacheBounds().getRight() - cacheFile.getIndexBounds().getLeft()) / reolustion) / cacheFile.getTileSize().value();
            double bottom = ((cacheFile.getIndexBounds().getTop() - cacheFile.getCacheBounds().getBottom()) / reolustion) / cacheFile.getTileSize().value();

            int tileLeft = (int) left;
            int tileTop = (int) top;

            int tileRight = (int) right;
            if ((right - tileRight) < 0.0001) {
                tileRight--;
            }
            int tileBottom = (int) bottom;
            if ((bottom - tileBottom) < 0.0001) {
                tileBottom--;
            }

            if (cacheFile.getStorageType() == StorageType.Original) {

            } else if (cacheFile.getStorageType() == StorageType.Compact) {
                result = result && checkCompactCache(cacheRoot, caption, tileLeft, tileTop, tileRight, tileBottom, reolustion, cacheFile);
            } else if (cacheFile.getStorageType() == StorageType.MongoDB) {
                result = result && checkMongoCache(manager, Integer.valueOf(caption), tileLeft, tileTop, tileRight, tileBottom, reolustion, cacheFile);
            }

            if (m_errorWriter != null) {
                try {
                    m_errorWriter.flush();
                    m_errorWriter.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                m_errorWriter = null;
            }
        }

        File filesci = new File(sciFile);
        String checkDir = (filesci.getParentFile().getParent() + "/checked");
        if (!result) {
            checkDir = (filesci.getParentFile().getParent() + "/error");
        }
        filesci.renameTo(new File(checkDir, filesci.getName()));

        if (manager != null) {
            manager.close();
        }
    }

    public void checkOriginalCache() {

    }

    public boolean checkCompactCache(String cacheRoot, String caption, int left, int top, int right, int bottom, double reolustion, CacheWriter cacheFile) {

        boolean isWithin = false;
        if (m_boundaryCheck) {
            Rectangle2D cacheBounds = cacheFile.getCacheBounds();
            Point2Ds points = new Point2Ds();
            points.add(new Point2D(cacheBounds.getLeft(), cacheBounds.getBottom()));
            points.add(new Point2D(cacheBounds.getLeft(), cacheBounds.getTop()));
            points.add(new Point2D(cacheBounds.getRight(), cacheBounds.getTop()));
            points.add(new Point2D(cacheBounds.getRight(), cacheBounds.getBottom()));
            GeoRegion region = new GeoRegion(points);

            isWithin = Geometrist.isWithin(region, m_boundaryRegion);
        }
        double anchorLeft = cacheFile.getIndexBounds().getLeft();
        double anchorTop = cacheFile.getIndexBounds().getTop();
        int tileSize = cacheFile.getTileSize().value();

        boolean result = true;
        for (int row = top; row <= bottom; ) {
            int bigRow = row / 128;
            int currentRow = Math.min(bottom, (bigRow + 1) * 128 - 1);

            for (int col = left; col <= right; ) {
                int bigCol = col / 128;
                int currentCol = Math.min(right, (bigCol + 1) * 128 - 1);

                String cfPath = cacheRoot + "/" + caption + "/" + bigRow + "/" + bigCol + ".cf";
                File cfFile = new File(cfPath);

                //long starttime = System.nanoTime();

                if (!cfFile.exists()) {
                    result = false;
                    LogWriter.getInstance().writelog("lost:" + cfPath);
                    this.writError("lost," + reolustion + "," + bigRow + "," + bigCol + "," + m_sciFilePath);
                } else {
                    CompactFile compactFile = new CompactFile();
                    if (compactFile.Open(cfPath, "") != 0) {
                        result = false;
                        LogWriter.getInstance().writelog("failure:" + cfPath);
                        this.writError("failure," + reolustion + "," + bigRow + "," + bigCol + "," + m_sciFilePath);
                    } else {
                        byte[] data = null;
                        for (int tempRow = row; tempRow <= currentRow; tempRow++) {
                            for (int tempCol = col; tempCol <= currentCol; tempCol++) {

                                data = compactFile.getAt(tempRow % 128, tempCol % 128);
                                if (data == null || data.length == 0) {
                                    result = false;
                                    LogWriter.getInstance().writelog("missing:" + caption + "," + tempRow + "," + tempCol + "," + m_sciFilePath);
                                    this.writError("missing," + reolustion + "," + tempRow + "," + tempCol + "," + m_sciFilePath);
                                } else if (data.length > 16) {
                                    try {
                                        ByteArrayInputStream byteStream = new ByteArrayInputStream(data, 12, data.length - 12);
                                        //MemoryCacheImageInputStream tileStream = new MemoryCacheImageInputStream(byteStream);
                                        BufferedImage image = null;
                                        try {
                                            image = ImageIO.read(byteStream);
                                        } catch (IOException e) {
                                            result = false;
                                            LogWriter.getInstance().writelog("error:" + caption + "," + tempRow + "," + tempCol + "," + m_sciFilePath);
                                            this.writError("error," + reolustion + "," + tempRow + "," + tempCol + "," + m_sciFilePath);
                                            byteStream.close();
                                            continue;
                                        }

                                        boolean tileIsWithin = isWithin;
                                        if (m_boundaryCheck && !isWithin) {

                                            double boundLeft = anchorLeft + reolustion * col * tileSize;
                                            double boundRight = boundLeft + reolustion * tileSize;
                                            double boundTop = anchorTop - reolustion * row * tileSize;
                                            double boundBottom = boundTop - reolustion * tileSize;

                                            Point2Ds points = new Point2Ds();
                                            points.add(new Point2D(boundLeft, boundBottom));
                                            points.add(new Point2D(boundLeft, boundTop));
                                            points.add(new Point2D(boundRight, boundTop));
                                            points.add(new Point2D(boundRight, boundBottom));
                                            GeoRegion tileRegion = new GeoRegion(points);

                                            tileIsWithin = Geometrist.isWithin(tileRegion, m_boundaryRegion);
                                        }

                                        if (m_boundaryCheck) {
                                            if (tileIsWithin && isBlockWhite(image)) {
                                                result = false;
                                                LogWriter.getInstance().writelog("white:" + caption + "," + tempRow + "," + tempCol + "," + m_sciFilePath);
                                                this.writError("white," + reolustion + "," + tempRow + "," + tempCol + "," + m_sciFilePath);
                                            }
                                        } else {
                                            if (isSolidWhite(image)) {
                                                result = false;
                                                LogWriter.getInstance().writelog("white:" + caption + "," + tempRow + "," + tempCol + "," + m_sciFilePath);
                                                this.writError("white," + reolustion + "," + tempRow + "," + tempCol + "," + m_sciFilePath);
                                            }
                                        }

                                        byteStream.close();
                                    } catch (IOException e) {
                                    }
                                }
                            }
                        }
                        compactFile.Close();
                    }
                }

                LogWriter.getInstance().flush();

                col = currentCol + 1;
            }
            row = currentRow + 1;
        }
        return result;
    }

    public boolean checkMongoCache(TileStorageManager manager, int level, int left, int top, int right, int bottom, double reolustion, CacheWriter cacheFile) {

        boolean isWithin = false;
        if (m_boundaryCheck) {
            Rectangle2D cacheBounds = cacheFile.getCacheBounds();
            Point2Ds points = new Point2Ds();
            points.add(new Point2D(cacheBounds.getLeft(), cacheBounds.getBottom()));
            points.add(new Point2D(cacheBounds.getLeft(), cacheBounds.getTop()));
            points.add(new Point2D(cacheBounds.getRight(), cacheBounds.getTop()));
            points.add(new Point2D(cacheBounds.getRight(), cacheBounds.getBottom()));
            GeoRegion region = new GeoRegion(points);

            isWithin = Geometrist.isWithin(region, m_boundaryRegion);
        }
        double anchorLeft = cacheFile.getIndexBounds().getLeft();
        double anchorTop = cacheFile.getIndexBounds().getTop();
        int tileSize = cacheFile.getTileSize().value();

        boolean result = true;
        byte[] data = null;
        TileContent content = null;
        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                //TileContentInfo info = new TileContentInfo(level,row,col,"");
                //content = manager.loadTile(info);
                content = manager.loadTile(level - 1, row, col);
                data = null;
                if (content != null) {
                    data = content.getData();
                    String key = content.getKey();
                    content.dispose();
                    if (!key.isEmpty()) {
                        continue;
                    }
                }

                if (data == null || data.length == 0) {
                    result = false;
                    LogWriter.getInstance().writelog("missing:" + level + "," + row + "," + col + "," + m_sciFilePath);
                    this.writError("missing," + reolustion + "," + row + "," + col + "," + m_sciFilePath);
                } else if (data.length > 16) {
                    try {
                        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
                        //MemoryCacheImageInputStream tileStream = new MemoryCacheImageInputStream(byteStream);
                        BufferedImage image = null;
                        try {
                            image = ImageIO.read(byteStream);
                        } catch (IOException e) {
                            result = false;
                            LogWriter.getInstance().writelog("error:" + level + "," + row + "," + col + "," + m_sciFilePath);
                            this.writError("error," + reolustion + "," + row + "," + col + "," + m_sciFilePath);
                            byteStream.close();
                            continue;
                        }

                        boolean tileIsWithin = isWithin;
                        if (m_boundaryCheck && !isWithin) {

                            double boundLeft = anchorLeft + reolustion * col * tileSize;
                            double boundRight = boundLeft + reolustion * tileSize;
                            double boundTop = anchorTop - reolustion * row * tileSize;
                            double boundBottom = boundTop - reolustion * tileSize;

                            Point2Ds points = new Point2Ds();
                            points.add(new Point2D(boundLeft, boundBottom));
                            points.add(new Point2D(boundLeft, boundTop));
                            points.add(new Point2D(boundRight, boundTop));
                            points.add(new Point2D(boundRight, boundBottom));
                            GeoRegion tileRegion = new GeoRegion(points);

                            tileIsWithin = Geometrist.isWithin(tileRegion, m_boundaryRegion);
                        }

                        if (m_boundaryCheck) {
                            if (tileIsWithin && isBlockWhite(image)) {
                                result = false;
                                LogWriter.getInstance().writelog("white:" + level + "," + row + "," + col + "," + m_sciFilePath);
                                this.writError("white," + reolustion + "," + row + "," + col + "," + m_sciFilePath);
                            }
                        } else {
                            if (isSolidWhite(image)) {
                                result = false;
                                LogWriter.getInstance().writelog("white:" + level + "," + row + "," + col + "," + m_sciFilePath);
                                this.writError("white," + reolustion + "," + row + "," + col + "," + m_sciFilePath);
                            }
                        }

                        byteStream.close();
                    } catch (IOException e) {
                    }
                } else {
                    //System.out.println(row + " " + col);
                }
            }
        }
        return result;
    }

    public double getReolustion(double scale, PrjCoordSys prjCoordSys, double cacheDPI) {
        double unitRatio = 0; // 单位：0.1mm/投影单位

        if (prjCoordSys.getCoordUnit().equals(Unit.DEGREE)) {
            unitRatio = prjCoordSys.getGeoCoordSys().getGeoDatum()
                    .getGeoSpheroid().getAxis()
                    * (prjCoordSys.getCoordUnit().value() - 1000000000)
                    * 10000
                    * 3.1415926535897932384626433833
                    / (180.0 * 1745329);
        } else {
            unitRatio = (double) prjCoordSys.getCoordUnit().value();
        }

        double resolution = 1 / (scale * cacheDPI / 254); // 单位:0.1mm/pixel
        resolution = resolution / unitRatio; // 单位:投影单位/pixel
        return resolution;
    }

    public boolean isSolidWhite(BufferedImage image) {
        byte[] data = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte value = -1;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != value) {
                return false;
            }
        }
        return true;
    }

    public boolean isBlockWhite(BufferedImage image) {
        byte[] data = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        byte value = -1;
        int whiteStart = 0, index = 0, whiteCount = 0, h, w;
        int widthByte = data.length / image.getHeight();
        for (h = 0; h < image.getHeight(); h++) {
            index = h * widthByte;
            for (w = 0; w < widthByte; w++) {
                if (data[index + w] == value) {
                    if (whiteStart == -1) {
                        whiteStart = w;
                    }
                    // 当连续白色超过指定值时，记录并换行
                    else if ((w - whiteStart) > 128) {
                        whiteCount++;
                        break;
                    }
                } else {
                    // 当不是连续白色时重置
                    whiteStart = -1;
                }
            }
            // 超过指定行有连续白线时退出
            if (whiteCount > 10) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getSciFileList(String scipath) {
        String checkDir = "";
        ArrayList<String> sciNames = new ArrayList<String>();
        if (scipath.endsWith(".list")) {
            File tasksFile = new File(scipath);
            String taskDir = tasksFile.getParent();
            BufferedReader taskReader;
            try {
                taskReader = new BufferedReader(new InputStreamReader(new FileInputStream(tasksFile), "UTF-8"));

                String line;
                while ((line = taskReader.readLine()) != null) {
                    sciNames.add(taskDir + "/" + line);
                }
                taskReader.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            checkDir = tasksFile.getParentFile().getParent() + "/checked";
        } else if (scipath.contains("&")) {
            String[] sciFileArray = scipath.split("&");
            for (String onesci : sciFileArray) {
                sciNames.add(onesci);
            }

            if (sciNames.size() > 0) {
                File sciFile = new File(sciNames.get(0));
                checkDir = sciFile.getParentFile().getParent() + "/checked";
            }
        } else if (scipath.endsWith(".sci")) {
            sciNames.add(scipath);

            File sciFile = new File(scipath);
            checkDir = sciFile.getParentFile().getParent() + "/checked";
        } else {
            File sciFile = new File(scipath);
            if (sciFile.isDirectory()) {
                String[] sciFiles = sciFile.list(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        return name.endsWith(".sci");
                    }
                });
                for (String name : sciFiles) {
                    sciNames.add(scipath + "/" + name);
                }

                checkDir = sciFile.getParent() + "/checked";
            }
        }

        File scicheck = new File(checkDir);
        if (!scicheck.exists()) {
            scicheck.mkdir();
        }
        File scierror = new File(scicheck.getParent() + "/error");
        if (!scierror.exists()) {
            scierror.mkdir();
        }

        return sciNames;
    }

    private void writError(String error) {

        if (!m_error2udb) {
            return;
        }

        if (m_errorWriter == null) {
            try {
                File file = new File(m_errorFileName);
                if (file.exists()) {
                    file.delete();
                } else {
                    file.getParentFile().mkdirs();
                }
                m_errorWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(m_errorFileName), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            m_errorWriter.write(error);
            m_errorWriter.newLine();
            m_errorWriter.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void Error2Udb(double anchorLeft, double anchorTop, int tileSize, String parentName) {
        String tempPath = parentName + "/temp/";
        File tempFile = new File(tempPath);
        if (!tempFile.exists()) {
            return;
        }
        String[] errorNames = tempFile.list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".err");
            }
        });

        if (errorNames.length <= 0) {
            System.out.println("no error file!");
            return;
        }

        String udbPath = parentName + "/udb/cache.udb";
        tempFile = new File(udbPath);
        if (!tempFile.exists()) {
            System.out.println("not found cache.udb file!");
            return;
        }
        DatasourceConnectionInfo info = new DatasourceConnectionInfo();
        info.setServer(udbPath);
        info.setAlias("cache");
        info.setEngineType(EngineType.UDB);

        Workspace workspace = new Workspace();
        Datasource ds = workspace.getDatasources().open(info);

        for (String name : errorNames) {

            int indexFirst = name.indexOf("_");
            int indexSecond = name.indexOf("_", indexFirst + 1);
            int indexThreed = name.indexOf("(");
            int indexFour = name.indexOf(")");
            String caption = name.substring(1, indexFirst);
            int bigRow = Integer.valueOf(name.substring(indexFirst + 2, indexSecond));
            int bigCol = Integer.valueOf(name.substring(indexSecond + 2, indexThreed));
            String datasetName = name.substring(indexThreed + 1, indexFour);

            ArrayList<String> addLines = new ArrayList<String>();
            ArrayList<String> modifyLines = new ArrayList<String>();

            File file = new File(tempPath + name);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

                String readline;
                while ((readline = reader.readLine()) != null) {
                    if (readline.startsWith("lost") || readline.startsWith("failure")) {
                        modifyLines.add(readline);
                    } else {
                        addLines.add(readline);
                    }
                }

                reader.close();
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            DatasetVector dataset = (DatasetVector) ds.getDatasets().get(datasetName);

            for (String line : modifyLines) {
                String[] elements = line.split(",");
                if (elements.length <= 0) {
                    continue;
                }
                //double reolustion = Double.valueOf(elements[1]);
                int row = Integer.valueOf(elements[2]);
                int col = Integer.valueOf(elements[3]);
                int errortype = elements[0].startsWith("lo") ? 3 : 4;

                String queryStr = "tiletype = 1 and tilerow = " + row + " and tilecol = " + col;
                Recordset recordset = dataset.query(queryStr, CursorType.DYNAMIC);
                recordset.edit();
                recordset.setFieldValue("errortype", errortype);
                recordset.setFieldValue("errordesc", elements[0]);
                recordset.update();
                recordset.close();
                recordset.dispose();
            }

            if (addLines.size() > 0) {
                Recordset recordset = dataset.getRecordset(false, CursorType.DYNAMIC);
                recordset.getBatch().setMaxRecordCount(10);
                recordset.getBatch().begin();

                for (String line : addLines) {
                    String[] elements = line.split(",");
                    if (elements.length <= 0) {
                        continue;
                    }
                    double reolustion = Double.valueOf(elements[1]);
                    int row = Integer.valueOf(elements[2]);
                    int col = Integer.valueOf(elements[3]);
                    int errortype = 5;
                    if (elements[0].startsWith("m")) {
                        errortype = 1;
                    } else if (elements[0].startsWith("w")) {
                        errortype = 2;
                    }

                    double boundLeft = anchorLeft + reolustion * col * tileSize;
                    double boundRight = boundLeft + reolustion * tileSize;
                    double boundTop = anchorTop - reolustion * row * tileSize;
                    double boundBottom = boundTop - reolustion * tileSize;

                    HashMap<String, Object> fieldInfo = new HashMap<String, Object>();
                    fieldInfo.put("tiletype", 2);
                    fieldInfo.put("tilerow", row);
                    fieldInfo.put("tilecol", col);
                    fieldInfo.put("errortype", errortype);
                    fieldInfo.put("errordesc", elements[0]);

                    // 在数据集中创建对应块
                    Point2Ds points = new Point2Ds();
                    points.add(new Point2D(boundLeft, boundBottom));
                    points.add(new Point2D(boundLeft, boundTop));
                    points.add(new Point2D(boundRight, boundTop));
                    points.add(new Point2D(boundRight, boundBottom));
                    GeoRegion region = new GeoRegion(points);

                    recordset.addNew(region, fieldInfo);
                }

                recordset.getBatch().update();
                recordset.close();
                recordset.dispose();
            }
            file.delete();
        }

        ds.close();
        workspace.close();
        workspace.dispose();
    }

}
