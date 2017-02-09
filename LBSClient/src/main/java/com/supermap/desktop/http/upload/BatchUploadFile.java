package com.supermap.desktop.http.upload;

import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.WebHDFS;
import com.supermap.desktop.http.CreateFile;
import com.supermap.desktop.http.LogUtils;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.utilities.ManagerXMLParser;
import com.supermap.desktop.utilities.StringUtilities;

import java.io.*;
import java.net.URLEncoder;

/**
 * @author xie
 */
public class BatchUploadFile extends Thread {
    // 文件信息
    private FileInfo uploadInfo;
    // 一组开始上传位置
    private long[] startPos;
    // 一组结束上传位置
    private long[] endPos;
    // 一组原始文件大小
    private long[] segmentLengths;
    // 休眠时间
    private static final int SLEEP_SECONDS = 1000;
    // 子线程上传
    private UploadFile[] fileItems;
    // 文件长度
    private long fileSize;
    // 是否第一个文件
    private boolean first = true;
    // 是否停止上传
    private boolean stop = false;
    // 临时文件信息
    private File tempFile;

    private int speed;

    public BatchUploadFile(FileInfo uploadInfo) {
        this.uploadInfo = uploadInfo;
        // 单线程实现上传
        // uploadInfo.setSplitter(1);
        // String tempPath = this.uploadInfo.getFilePath() + File.separator + uploadInfo.getFileName() + ".position";
        // tempFile = new File(tempPath);
        // 如果存在读入点位置的文件
        // if (tempFile.exists()) {
        // first = false;
        // // 就直接读取内容
        // try {
        // // readPosInfo();
        // // 文件长度直接取自最后一段的结束位置
        // fileSize = endPos[endPos.length - 1];
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // } else {
        // 数组的长度就要分成多少段的数量
        // 上传时利用单线程实现
        startPos = new long[1];
        endPos = new long[1];
        segmentLengths = new long[1];

        // }
    }

    @Override
    public void run() {
        try {
            // 首次上传，获取上传文件长度
            if (first) {
                /**
                 * eg start: 1, 3, 5, 7, 9 end: 3, 5, 7, 9, length
                 */
                for (int i = 0, len = this.segmentLengths.length; i < len; i++) {
                    int size = (int) (i * (uploadInfo.getFileSize() / len));
                    this.startPos[i] = size;

                    // 设置最后一个结束点的位置
                    if (i == len - 1) {
                        this.endPos[i] = uploadInfo.getFileSize();
                    } else {
                        size = (int) ((i + 1) * (uploadInfo.getFileSize() / len));
                        this.endPos[i] = size;
                    }
                    this.segmentLengths[i] = this.endPos[i] - this.startPos[i];
                    LogUtils.log("start-end Position[" + i + "]: " + this.startPos[i] + "-" + this.endPos[i]);
                }
            }
            if (!stop) {
                // 创建一个空文件
                String locationURL = new CreateFile(uploadInfo).createFile();

                if (!StringUtilities.isNullOrEmpty(locationURL)) {
                    File uploadFile = new File(this.uploadInfo.getFilePath() + File.separator + this.uploadInfo.getFileName());
                    fileSize = uploadFile.length();
                    String fileName = URLEncoder.encode(this.uploadInfo.getFileName(), "UTF-8");
                    String url = this.uploadInfo.getUrl();
                    if (!url.endsWith("/")) {
                        url += "/";
                    }
                    int buffersize = Integer.parseInt(WebHDFS.getFileStatus(url, this.uploadInfo.getFileName()).getSize());
                    String webFile = url + fileName + "?user.name=root&op=APPEND";
                    // 创建单线程下载对象数组
                    fileItems = new UploadFile[this.segmentLengths.length];
                    for (int i = 0; i < this.segmentLengths.length; i++) {
                        // 创建指定个数单线程下载对象，每个线程独立完成指定块内容的下载
                        startPos[i] = buffersize;
                        endPos[i] = fileSize;
                        segmentLengths[i] = fileSize;
                        fileItems[i] = new UploadFile(webFile, uploadFile, startPos[i], endPos[i], segmentLengths[i], i);
                        fileItems[i].start();
                        Application.getActiveApplication().getOutput().output("Thread: " + i + ", startPos: " + startPos[i] + ", endPos: " + endPos[i]);
                    }

                    // 循环写入下载文件长度信息
                    Boolean isFinished = false;

                    while (!stop && !isFinished) {
                        try {
                            LogUtils.log("uploading……");
                            // writePosInfo();
                            getSpeed();
                            UploadUtils.fireSteppedEvent(this, uploadInfo, this.getUploadProcess(), this.getRemainTime());
                            isFinished = true;
                            Thread.sleep(SLEEP_SECONDS);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            isFinished = this.isFinished();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // 上传完成了删除进度文件
                        if (isFinished) {
                            UploadUtils.fireSteppedEvent(this, uploadInfo, 100, 0);
                            Application.getActiveApplication().getOutput()
                                    .output(this.uploadInfo.getFileName() + " " + LBSClientProperties.getString("String_UploadEnd"));
                            ManagerXMLParser.removeTask(TaskEnum.UPLOADTASK, uploadInfo.getUrl(), uploadInfo.getFileName());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void getSpeed() {
        this.speed = 0;
        if (null != fileItems) {
            for (int i = 0; i < fileItems.length; i++) {
                speed += fileItems[i].getSpeed();
            }
            speed = speed / fileItems.length;
        }
    }

    /**
     * 获取文件的上传完成的大小
     */
    public long getFinishedSize() throws IOException {
        long finished = 0;
        try {
            if (fileItems != null) {
                for (int i = 0; i < fileItems.length; i++) {
                    if (fileItems[i] != null) {
                        finished += fileItems[i].getLength() - (fileItems[i].getEndPos() - fileItems[i].getStartPos());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return finished;
    }

    /**
     * 获取文件的上传进度
     */
    private int getUploadProcess() throws IOException {
        int process = 0;
        try {
            long finished = this.getFinishedSize();
            if (this.uploadInfo.getFileSize() != 0) {
                process = (int) (finished * 100 / this.uploadInfo.getFileSize());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return process;
    }

    /**
     * 获取文件的剩余上传时间
     */
    private int getRemainTime() throws IOException {
        int remainTime = 0;
        try {
            long finished = this.getFinishedSize();
            if (this.speed != 0) {
                remainTime = (int) ((this.uploadInfo.getFileSize() - finished) / (this.speed));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return remainTime;
    }

    /**
     * 获取文件的上传信息
     */
    public String getDownloadInformation() throws IOException {
        String information = "";
        try {
            information = String.format("%s/%s", convertFileSize(this.getFinishedSize()), convertFileSize(this.fileSize));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return information;
    }

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    /**
     * 获取文件的上传进度
     */
    public Boolean isFinished() throws IOException {
        Boolean result = true;
        try {
            if (fileItems != null) {
                for (int i = 0; i < fileItems.length; i++) {
                    if (fileItems[i] != null && !fileItems[i].isUploadOver()) {
                        result = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 停止上传
     */
    public void stopUpload() throws IOException {
        this.stop = true;
        if (null != fileItems) {
            for (int i = 0; i < fileItems.length; i++) {
                fileItems[i].stopUpload();
            }
        }
    }

    /**
     * 继续上传
     */
    public void resumeDownload() {
        try {
            this.stop = false;
            readPosInfo();
            this.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 将写入点数据保存在临时文件中
     */
    private void writePosInfo() throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(tempFile));
        dos.writeInt(startPos.length);
        for (int i = 0; i < startPos.length; i++) {
            dos.writeLong(fileItems[i].getStartPos());
            dos.writeLong(fileItems[i].getEndPos());
            dos.writeLong(fileItems[i].getLength());
            // LogUtils.info("[" + fileItem[i].getStartPos() + "#" + fileItem[i].getEndPos() + "]");
        }
        dos.close();
    }

    /**
     * <b>function:</b>读取写入点的位置信息
     */
    private void readPosInfo() throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(tempFile));
        int startPosLength = dis.readInt();
        startPos = new long[startPosLength];
        endPos = new long[startPosLength];
        segmentLengths = new long[startPosLength];
        for (int i = 0; i < startPosLength; i++) {
            startPos[i] = dis.readLong();
            endPos[i] = dis.readLong();
            segmentLengths[i] = dis.readLong();
        }
        dis.close();
    }

    public FileInfo getDownloadInfo() {
        return uploadInfo;
    }

    public void setDownloadInfo(FileInfo downloadInfo) {
        this.uploadInfo = downloadInfo;
    }

}