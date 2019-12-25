package com.seasoning.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
public class FileUtil {
	private static final Logger logger = LogManager.getLogger(FileUtil.class);

	public static byte[] load(String fileName) {
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(fileName))); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			int tmp;
			while ((tmp = in.read()) != -1) {
				out.write(tmp);
			}
			return out.toByteArray();
		} catch (Exception e) {
			logger.error(e.getMessage(), fileName);
			return null;
		}
	}
	
	 /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader  reader = new BufferedReader(new FileReader(file))){
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
            	builder.append(tempString);
            }
        } catch (IOException e) {
        	logger.error("找不到指定文件！path="+file.getAbsolutePath(), e);
        }
        return builder.toString();
    }

	public static boolean copyFile(MultipartFile sourceFile, String targetFile){
		File file = new File(targetFile);
		try(InputStream inputStream = sourceFile.getInputStream();
			OutputStream outputStream = new FileOutputStream(file)){
			byte[] bs = new byte[1024];
			int res = 0;
			while((res = inputStream.read(bs)) != -1){
				outputStream.write(bs, 0, res);
			}
		}catch (IOException e){
			log.error("写入文件失败!", e);
			return false;
		}
		return true;
	}

}
