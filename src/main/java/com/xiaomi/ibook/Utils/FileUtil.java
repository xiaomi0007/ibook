package com.xiaomi.ibook.Utils;

import com.google.common.io.Closeables;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaomi
 * on 2020.04.16 11:30:00
 */
@Component
@Slf4j
public class FileUtil {

    private static final String SUFFIX_ENCODE_FILE = ".encode";


    @Resource
    private AESUtil aesUtil;

    public Charset getFileEncode(File file) throws Exception{
        Charset charset = StandardCharsets.UTF_8;
        if(isFileEncode(file,charset)){
            return charset;
        }
        charset = StandardCharsets.UTF_16;
        if(isFileEncode(file,charset)){
            return charset;
        }
        charset = Charset.forName(C.CHARSET_GBK);
        if(isFileEncode(file,charset)){
            return charset;
        }
        return null;
    }

    public int getCount(File file) throws Exception{
        List<String> strings = Files.readLines(file, StandardCharsets.UTF_8);
        int count = 0;
        String key = aesUtil.decrypt("6DEE57F78E23A99447A0D1810F9D50F3", C.PASSWORD);
        for(String s : strings){
            count += StringUtil.getSubCount(s,key);
        }
        return count;
    }

    /**
     * 用文本中包含的“的”字数量判断文本的编码格式是否是指定的编码格式
     * 在使用时可以先把FALSE的文本rename到其他位置，再用notepad打开人工看看编码格式
     * 用这个方法可以批量查看文本的编码格式
     *
     * @param file 需要检查的文本
     * @param charset 需要验证的编码格式
     * @return 是否为参数指定的编码格式
     * @throws Exception 异常
     */
    public Boolean isFileEncode(File file, Charset charset) throws Exception{
        List<String> filelist = Files.readLines(file, charset);
        for(String s : filelist){
            if(s.contains("的")){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public List<File> searchFiles(File folder, final String keyword) {
        List<File> result = new ArrayList<>();
        if (folder.isFile()){
            result.add(folder);
        }
        File[] subFolders = folder.listFiles((file)->{
            if (file.isDirectory()) {
                return true;
            }
            return file.getName().toLowerCase().contains(keyword) && !file.getName().endsWith(C.SUFFIX_TEMP);
        });
        if (subFolders != null) {
            for (File file : subFolders) {
                if (file.isFile() ) {
                    // 如果是文件则将文件添加到结果列表中
                    log.info("按关键字["+keyword+"]搜索到:" + file.getAbsolutePath());
                    result.add(file);
                } else {
                    // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
                    result.addAll(searchFiles(file, keyword));
                }
            }
        }
        return result;
    }

    public void saveFileByUtf8(List<String> readLines, String filename) {
        try {
            File file = new File(filename);
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            StringBuilder txt = new StringBuilder("");
            for(String line : readLines){
                line = line + "\r\n";
                txt.append(line);
            }
            Files.write(txt.toString().getBytes(StandardCharsets.UTF_8),file);
        } catch (IOException e) {
            log.error("修改文件格式保存异常",e);
        }
    }

    /**
     * 把字符串写入到指定目录
     * @param filestring 文本内容
     * @param target 目标文件
     */
    public void setString2File(String filestring,File target) throws Exception{
        BufferedWriter out = null;
        try{
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target),StandardCharsets.UTF_8.name()));
            out.write(filestring);
            out.flush();
        }catch(Exception e){
            log.error("",e);
        }finally{
            Closeables.close(out,Boolean.TRUE);
        }
    }

    public void paragraphFormat(String from,String filenameNoSuffix) throws Exception{
        File file = new File(from);
        List<String> stringList = Files.readLines(file, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for(String s : stringList){
            s = s.replace((char)12288, ' ');
            String line = StringUtil.replaceBlank(s);
            line = line.trim();
            if (!StringUtils.isBlank(line)) {
                if(StringUtil.isEndLine(line,filenameNoSuffix)){
                    sb.append(line + "\r\n    ");
                }else {
                    sb.append(line);
                }
            }
        }
        File temppath = new File(from + C.SUFFIX_TEMP);
        setString2File(sb.toString(),temppath);
        file.delete();
        temppath.renameTo(file.getAbsoluteFile());
    }

    public void changeFileEncode(File file) throws Exception{
        Charset charset = getFileEncode(file);
        if(StandardCharsets.UTF_8.equals(charset)){
            log.info(file.getAbsolutePath() + "编码格式为UTF-8，无需处理");
        }else if (charset == null){
            log.info(file.getAbsolutePath() + "无法识别编码格式");
            throw new Exception("无法识别编码格式");
        }else {
            List<String> strings = Files.readLines(file,charset);
            File temp = new File(file.getAbsolutePath() + SUFFIX_ENCODE_FILE);
            saveFileByUtf8(strings, temp.getAbsolutePath());
            file.delete();
            temp.renameTo(new File(file.getAbsolutePath()));
        }
    }


    private String readFileContent(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String content = "";
        StringBuilder sb = new StringBuilder();
        while(content != null){
            content = bf.readLine();
            if(content == null){
                break;
            }
            sb.append(content.trim());
        }
        bf.close();
        return sb.toString();
    }
}
