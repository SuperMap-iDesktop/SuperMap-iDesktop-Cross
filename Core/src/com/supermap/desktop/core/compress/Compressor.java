package com.supermap.desktop.core.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.event.EventListenerList;

import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.http.TruncatedChunkException;

import com.supermap.desktop.Application;
import com.supermap.desktop.core.FileSize;
import com.supermap.desktop.core.FileSizeType;
import com.supermap.desktop.utilties.FileUtilities;
import com.supermap.desktop.utilties.ListUtilities;
import com.supermap.desktop.utilties.StringUtilities;

/**
 * 封装文件/文件夹的压缩，提供进度信息的支持（目前仅实现 zip）
 * 
 * @author highsad
 *
 */
public class Compressor {
	private static final String ZIP_NAME_FORMAT = "{0}{1}zip";
	private static int BUFFER_SIZE = 1024;

	private EventListenerList listenerList = new EventListenerList();

	private String desDir = "";
	private String desName = "";
	private File srcFile;
	private long totalSize = 0;
	private long compressedSize = 0;
	private int totalFileEntry = 0; // 待压缩项的文件总数
	private int currentFileEntry = 0; // 当前正在压缩的文件项
	private boolean isCancel = false;

	// @formatter:off
	/**
	 * 是否将源文件目录的根目录作为压缩包的第一级
	 * 1.	值为 false，则压缩包没有第一级结构，解压之后的目录结构与源文件夹目录结构一致。
	 * 2.	值为 true，则压缩包使用源文件目录的根目录作为第一级结构，解压之后的目录中只有一个目录，目录名与源文件目录的根目录一致。
	 */
	// @formatter:on
	private boolean isRootAsTopEntity = true;
	/**
	 * 待压缩的文件。如果 srcFile 是目录，那么该目录下的文件支持选择压缩一部分。
	 */
	private ArrayList<File> files = new ArrayList<>();

	/**
	 * @param srcFile
	 *            需要压缩的源文件或者源文件夹
	 * @param desDir
	 *            保存压缩结果的目录
	 */
	public Compressor(String srcFile, String desDir) {
		this(srcFile, desDir, true);
	}

	/**
	 * @param srcFile
	 *            需要压缩的源文件或者源文件夹
	 * @param desDir
	 *            保存压缩结果的目录
	 * @param isRootAsTopEntity
	 *            是否使用源文件目录结构根目录作为压缩包的第一级
	 */
	public Compressor(String srcFile, String desDir, boolean isRootAsTopEntity) {
		// 最后一个参数传递 null 会与 Compressor(String srcFile, String desDir, String desName , boolean isRootAsTopEntity) 冲突
		this(srcFile, desDir, new ArrayList<File>(), isRootAsTopEntity);
	}

	/**
	 * @param srcFile
	 *            需要压缩的源文件或者源文件夹
	 * @param desDir
	 *            保存压缩结果的目录
	 * @param files
	 *            指定需要压缩的文件
	 */
	public Compressor(String srcFile, String desDir, ArrayList<File> files) {
		this(srcFile, desDir, files, true);
	}

	/**
	 * @param srcFile
	 *            需要压缩的源文件或者源文件夹
	 * @param desDir
	 *            保存压缩结果的目录
	 * @param files
	 *            指定需要压缩的文件
	 * @param isRootAsTopEntity
	 *            是否使用源文件目录结构根目录作为压缩包的第一级
	 */
	public Compressor(String srcFile, String desDir, ArrayList<File> files, boolean isRootAsTopEntity) {
		this.srcFile = new File(srcFile);
		this.desDir = desDir;
		this.isRootAsTopEntity = isRootAsTopEntity;
		getDesName();
		initializeCompressFiles(files);
	}

	/**
	 * @param srcFile
	 *            需要压缩的源文件或者源文件夹
	 * @param desDir
	 *            保存压缩结果的目录
	 * @param desName
	 *            压缩包文件名
	 */
	public Compressor(String srcFile, String desDir, String desName) {
		this(srcFile, desDir, desName, true);
	}

	/**
	 * @param srcFile
	 *            需要压缩的源文件或者源文件夹
	 * @param desDir
	 *            保存压缩结果的目录
	 * @param desName
	 *            压缩包文件名
	 * @param isRootAsTopEntity
	 *            是否使用源文件目录结构根目录作为压缩包的第一级
	 */
	public Compressor(String srcFile, String desDir, String desName, boolean isRootAsTopEntity) {
		this(srcFile, desDir, desName, null, isRootAsTopEntity);
	}

	/**
	 * @param srcFile
	 *            需要压缩的源文件或者源文件夹
	 * @param desDir
	 *            保存压缩结果的目录
	 * @param desName
	 *            压缩包文件名
	 * @param files
	 *            指定将要压缩的文件
	 */
	public Compressor(String srcFile, String desDir, String desName, ArrayList<File> files) {
		this(srcFile, desDir, desName, files, true);
	}

	/**
	 * @param srcFile
	 *            需要压缩的源文件或者源文件夹
	 * @param desDir
	 *            保存压缩结果的目录
	 * @param desName
	 *            压缩包文件名
	 * @param files
	 *            指定将要压缩的文件
	 * @param isRootAsTopEntity
	 *            是否使用源文件目录结构根目录作为压缩包的第一级
	 */
	public Compressor(String srcFile, String desDir, String desName, ArrayList<File> files, boolean isRootAsTopEntity) {
		this.srcFile = new File(srcFile);
		this.desDir = desDir;
		this.isRootAsTopEntity = isRootAsTopEntity;

		if (StringUtilities.isNullOrEmpty(desName)) {
			getDesName();
		} else {
			if (desName.toLowerCase().endsWith(ArchiveStreamFactory.ZIP)) {

				// 如果指定的结果名是 zip 格式，那么就直接使用（其实还需要确认 zip 前的分隔符是否符合当前文件系统）
				this.desName = desName;
			} else {
				this.desName = MessageFormat.format(ZIP_NAME_FORMAT, desName, ".");
			}
		}
		initializeCompressFiles(files);
	}

	/**
	 * 开始压缩
	 * 
	 * @throws IOException
	 */
	public String compress() throws IOException {
		ZipArchiveOutputStream zipArchiveOutputStream = null;
		String desFile = this.desDir + File.separator + this.desName;

		try {
			ArchiveStreamFactory factory = new ArchiveStreamFactory();
			zipArchiveOutputStream = (ZipArchiveOutputStream) factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new FileOutputStream(
					new File(desFile)));

			String entryName = "";
			if (this.srcFile.isDirectory() && !this.isRootAsTopEntity) {
				// 源文件为目录，且不使用源文件目录的根目录作为压缩包的第一级
				entryName = "";
			} else {
				entryName = this.srcFile.getName() + File.separator;
				ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(entryName);
				zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
				zipArchiveOutputStream.closeArchiveEntry();
			}

			for (int i = 0; i < this.files.size(); i++) {
				compress(zipArchiveOutputStream, this.files.get(i), entryName);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			desFile = "";
		} finally {
			zipArchiveOutputStream.close();
			this.isCancel = false;
			this.compressedSize = 0;
		}
		return desFile;
	}

	public void addCompressingListener(CompressListener listener) {
		this.listenerList.add(CompressListener.class, listener);
	}

	public void removeCompressingListener(CompressListener listener) {
		this.listenerList.remove(CompressListener.class, listener);
	}

	protected void fireCompressing(CompressEvent e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CompressListener.class) {
				((CompressListener) listeners[i + 1]).compressing(e);
			}
		}
		this.isCancel = e.isCancel();
	}

	/**
	 * 压缩文件
	 * 
	 * @param zipArchiveOutputStream
	 * @param srcFile
	 *            源文件
	 * @param parentEntry
	 *            父节点名称
	 * @throws IOException
	 */
	private void compress(ZipArchiveOutputStream zipArchiveOutputStream, File srcFile, String parentEntry) throws IOException {
		if (srcFile == null || !srcFile.exists() || this.isCancel) {
			return;
		}

		if (srcFile.isDirectory()) {
			compressDir(zipArchiveOutputStream, srcFile, parentEntry);
		} else {
			compressFile(zipArchiveOutputStream, srcFile, parentEntry);
		}
	}

	/**
	 * 压缩目录
	 * 
	 * @param zipArchiveOutputStream
	 * @param srcFile
	 * @param parentEntry
	 * @throws IOException
	 */
	private void compressDir(ZipArchiveOutputStream zipArchiveOutputStream, File srcFile, String parentEntry) throws IOException {
		String entryName = parentEntry + srcFile.getName() + File.separator;
		ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(entryName);
		zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
		zipArchiveOutputStream.closeArchiveEntry();

		File[] files = srcFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			compress(zipArchiveOutputStream, files[i], entryName);
		}
	}

	/**
	 * 压缩文件
	 * 
	 * @param zipArchiveOutputStream
	 * @param srcFile
	 * @param parentEntry
	 * @throws IOException
	 */
	private void compressFile(ZipArchiveOutputStream zipArchiveOutputStream, File srcFile, String parentEntry) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(srcFile);

		try {
			String entryName = parentEntry + srcFile.getName();
			ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(entryName);
			zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);

			long totalSize = 0; // 已经读取的字节数
			byte[] buffer = new byte[BUFFER_SIZE];

			while (totalSize < srcFile.length() && !this.isCancel) {
				int readSize = fileInputStream.read(buffer);

				if (readSize > 0) {
					totalSize += readSize;
					zipArchiveOutputStream.write(buffer, 0, readSize);

					compressedSize += readSize;
					CompressEvent event = new CompressEvent(this, new FileSize(this.totalSize, FileSizeType.BYTE), new FileSize(compressedSize,
							FileSizeType.BYTE), this.currentFileEntry + 1, this.totalFileEntry);
					fireCompressing(event);
					if (event.isCancel()) {
						this.isCancel = true;
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.currentFileEntry++;
			zipArchiveOutputStream.closeArchiveEntry();
			fileInputStream.close();
		}
	}

	/**
	 * 根据原文件名获取合适的目标文件名
	 */
	private void getDesName() {
		if (this.srcFile != null && this.srcFile.exists()) {
			if (this.srcFile.isDirectory()) {

				// 源文件是目录，那么就使用 目录名+分隔符+zip 作为结果文件名
				this.desName = MessageFormat.format(ZIP_NAME_FORMAT, this.srcFile.getName(), ".");
			} else {
				String srcFileName = this.srcFile.getName();

				// 源文件带后缀，那么使用 无后缀文件名+分隔符+zip 作为结果文件名
				if (srcFileName.indexOf(".") > 0) {
					String[] tmp = srcFileName.split(".");

					this.desName = MessageFormat.format(ZIP_NAME_FORMAT, tmp[0], ".");
				} else {

					// 源文件不带后缀，那么使用 文件名+分隔符+zip 作为结果文件名
					this.desName = MessageFormat.format(ZIP_NAME_FORMAT, this.srcFile.getName(), ".");
				}
			}
		}
	}

	/**
	 * 初始化将要压缩的文件信息
	 * 
	 * @param files
	 *            指定需要压缩的文件，不指定则默认 srcFile下的所有。
	 */
	private void initializeCompressFiles(ArrayList<File> files) {
		if (files != null && files.size() > 0) {
			for (int i = 0; i < files.size(); i++) {
				File file = files.get(i);

				if (file.getParentFile().equals(this.srcFile)) {
					this.files.add(file);
				}
			}
		} else {
			ListUtilities.addArray(this.files, this.srcFile.listFiles());
		}

		computeTotalSize();
		computeTotalFileEntryCount();
	}

	/**
	 * 计算带压缩的文件总数（所有单个文件）
	 */
	private void computeTotalFileEntryCount() {
		for (int i = 0; i < this.files.size(); i++) {
			this.totalFileEntry += getFileEntryCount(this.files.get(i));
		}
	}

	private int getFileEntryCount(File file) {
		int result = 0;
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] childFiles = file.listFiles();
				for (int i = 0; i < childFiles.length; i++) {
					result += getFileEntryCount(childFiles[i]);
				}
			} else {
				result = 1;
			}
		}
		return result;
	}

	/**
	 * 计算将要压缩的文件总大小
	 */
	private void computeTotalSize() {
		for (int i = 0; i < this.files.size(); i++) {
			this.totalSize += FileUtilities.getFileSize(this.files.get(i));
		}
	}
}
