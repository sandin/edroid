package com.edroid.utils;

public class TextUtil {
    
    /////////// truncate ///////////////////
    
    /**
     * 如果文字过长，则截取前length个字符，并加endStr作为省略符
     * 
     * @param target
     * @param length
     * @param endStr
     * @return
     */
    public static String truncate(String target, int length, String endStr) {
        if (target == null) {
            return "";
        }
        if (target.length() > length) {
            if (endStr != null) {
                return target.substring(0, length) + endStr;
            } else {
                return target.substring(0, length);
            }
        }
        return target;
    }
    
    /**
     * 截取字符串 中文算1个字符，英文算半个字符
     * 
     * @param target
     * @param length
     * @param endStr
     * @return
     */
    public static String truncate2(String target, int length, String endStr) {
        if (target == null) {
            return "";
        }
        
        String result = cutMultibyte(target, length*2);
        if (result.length() < target.length()) {
            return result + endStr;
        }
        return result;
    }
    
    /**  
     * 截取一段字符的长度(汉、日、韩文字符长度为2),不区分中英文,如果数字不正好，则少取一个字符位  
     *   
     * @param str 原始字符串  
     * @param srcPos 开始位置
     * @param specialCharsLength 截取长度(汉、日、韩文字符长度为2)  
     * @return  
     */   
    public static String cutMultibyte(String str,int specialCharsLength) {   
        int srcPos = 0;
        if (str == null || "".equals(str) || specialCharsLength < 1) {   
            return "";   
        }
        if(srcPos<0)
        {
         srcPos=0;
        }
        if(specialCharsLength<=0)
        {
         return "";
        }
        //获得字符串的长度
        char[] chars = str.toCharArray();
        if(srcPos>chars.length)
        {
         return "";
        }      
        int charsLength = getCharsLength(chars, specialCharsLength);       
        return new String(chars, srcPos, charsLength);   
    }   
   
    /**  
     * 获取一段字符的长度，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1  
     * @param chars 一段字符  
     * @param specialCharsLength 输入长度，汉、日、韩文字符长度为2  
     * @return 输出长度，所有字符均长度为1  
     */   
    private static int getCharsLength(char[] chars, int specialCharsLength) {    
        int count = 0;   
        int normalCharsLength = 0;   
        for (int i = 0; i < chars.length; i++) {   
            int specialCharLength = getSpecialCharLength(chars[i]);           
            if (count <= specialCharsLength - specialCharLength) {            
                count += specialCharLength;   
                normalCharsLength++;   
            } else {   
                break;   
            }   
        }       
        return normalCharsLength;   
    }   
   
    /**  
     * 获取字符长度：汉、日、韩文字符长度为2，ASCII码等字符长度为1  
     * @param c 字符  
     * @return 字符长度  
     */   
    private static int getSpecialCharLength(char c) {   
        if (isLetter(c)) {   
            return 1;   
        } else {   
            return 2;   
        }   
    } 
    
    /**  
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）  
     *   
     * @param c 需要判断的字符  
     * @return 返回true,Ascill字符  
     */   
    public static boolean isLetter(char c) {   
        int k = 0x80;   
        return c / k == 0 ? true : false;   
    } 
    
    //////////////////////////////

}
