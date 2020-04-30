package com.xiaomi.ibook.Utils;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaomi
 * on 20/04/20 15:46:00
 */
public class StringUtil {

    /**
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 如果两个字符串中任意一个是另一个的子串，则返回这个子串，如果没有，则返回空
     */
    public static String isSameName(String str1,String str2){
        str1 = str1.trim().toLowerCase().replace(C.SUFFIX_TXT,"");
        str2 = str2.trim().toLowerCase().replace(C.SUFFIX_TXT,"");
        if(str1.length() >= str2.length()){
            if(str1.contains(str2)){
                return str2;
            }
        }else {
            if(str2.contains(str1)){
                return str1;
            }
        }
        return "";
    }

    /**
     * 格式化数据,保留小数点后N位
     * @param rateStr 需要处理的数据
     * @param length 保留到小数点后多少位
     * @return 处理完的数据
     * @throws Exception 异常
     */
    public static String formatRate(String rateStr,Integer length) throws Exception{
        Double d = Double.parseDouble(rateStr);
        String pattern = "#";
        if(length >=1){
            pattern += ".";
            for(Integer i=0;i< length;i++){
                pattern += "0";
            }
        }
        DecimalFormat f = new DecimalFormat(pattern);
        String s = f.format(d);
        if(s.startsWith(".")){
            s = "0" + s;
        }
        return s;
    }



    /**
     * 统计一个字符串中出现指定字符串多少次
     * @param srcstr 需要检查的字符串
     * @param findstr 指定的字符
     * @return 出现的次数
     */
    public static Integer count(String srcstr,String findstr){
        Integer count = 0;
        Pattern p = Pattern.compile(findstr);
        Matcher m = p.matcher(srcstr);
        while(m.find()){
            count ++ ;
        }
        return count;
    }


    /**
     *
     * @param str 需要判断的字符串
     * @param pattern 中文 "[\u4e00-\u9fa5]"; 数字 "[0-9]"
     * @return
     */
    public static Boolean isMatches(String str,String pattern){
        Pattern p = Pattern.compile(pattern);
        for(int i = 0;i<str.length();i++) {
            String s = String.valueOf(str.charAt(i));
            return p.matcher(s).matches();
        }
        return Boolean.FALSE;
    }


    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    public static Boolean hasSameSubString(String str1,String str2,int length){
        for(int i = 0;i<str1.length() - length + 1;i++){
            String temp = str1.substring(i,i + length);
            if(str2.contains(temp)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private static final List<String> PUNCTUATIONS = ImmutableList.of("」","。","。。。","！","？","；","”","\"",".","…","...","?","!",";");

    public static Boolean isEndLine(String line,String filenameNoSuffix){
        if(StringUtils.isBlank(line)){
            return Boolean.FALSE;
        }
        line = line.trim();
        boolean isTitle = line.startsWith("第") && (line.contains("篇")|| line.contains("段")|| line.contains("卷")  || line.contains("章") || line.contains("节") || line.contains("部"));
        boolean startWithFilename = line.startsWith(filenameNoSuffix);
        if((isTitle || startWithFilename) && line.length() <50){
            return Boolean.TRUE;
        }
        String endChar = line.substring(line.trim().length()-1);
        return PUNCTUATIONS.contains(endChar);
    }

    public static int getSubCount(String str, String key) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();
            count++;
        }
        return count;
    }


    public static void main(String[] args) {
        String s ="累得厉害，也就昏昏睡去…… \n";
        String ss = replaceBlank(s);
        String sss = ss.trim();
        String endChar = sss.substring(sss.trim().length()-1);
        System.out.println(endChar);
        System.out.println(isEndLine(sss, IdUtil.fastSimpleUUID()));
    }

}
