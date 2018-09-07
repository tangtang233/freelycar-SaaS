package com.freelycar.basic.persistence.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;
import java.io.*;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils {
    public StringUtil() {
    }

    public static String getStr(Object src) {
        return getStr(src, -1);
    }

    public static String getTrimedStr(Object src) {
        return getTrimedStr(src, -1);
    }

    public static String getStr(Object src, String defaultValue) {
        return getStr(src, -1, defaultValue);
    }

    public static String getTrimedStr(Object src, String defaultValue) {
        return getTrimedStr(src, -1, defaultValue);
    }

    public static String getStr(Object src, int length) {
        return getStr(src, 0, length);
    }

    public static String getTrimedStr(Object src, int length) {
        return getTrimedStr(src, 0, length);
    }

    public static String getStr(Object src, int length, String defaultValue) {
        return getStr(src, 0, length, defaultValue);
    }

    public static String getTrimedStr(Object src, int length, String defaultValue) {
        return getTrimedStr(src, 0, length, defaultValue);
    }

    public static String getStr(Object src, int start, int length) {
        return getStr(src, start, length, "", false);
    }

    public static String getTrimedStr(Object src, int start, int length) {
        return getStr(src, start, length, "", true);
    }

    public static String getStr(Object src, int start, int length, String defaultValue) {
        return getStr(src, start, length, defaultValue, false);
    }

    public static String getTrimedStr(Object src, int start, int length, String defaultValue) {
        return getStr(src, start, length, defaultValue, true);
    }

    public static String getStr(Object src, int start, int length, String defaultValue, boolean trim) {
        if (src == null) {
            return defaultValue;
        } else {
            String value = src.toString();
            if (value.length() > start) {
                if (length >= 0 && value.length() >= start + length) {
                    value = value.substring(start, start + length);
                } else {
                    value = value.substring(start);
                }
            } else {
                value = "";
            }

            return trim ? value.trim() : value;
        }
    }

    public static String fromBlob(Blob blob) {
        if (blob == null) {
            return "";
        } else {
            try {
                byte[] array = new byte[1000];
                InputStream in = blob.getBinaryStream();
                Vector<byte[]> v = new Vector();
                Vector<Integer> v1 = new Vector();
                int len = in.read(array, 0, 1000);

                int alllen;
                for (alllen = 0; len > 0; len = in.read(array, 0, 1000)) {
                    alllen += len;
                    v.add(array);
                    v1.add(new Integer(len));
                    array = new byte[1000];
                }

                if (alllen > 0 && v.size() > 0) {
                    byte[] arraytemp = new byte[alllen];
                    int index = 0;

                    for (int i = 0; i < v.size(); ++i) {
                        int lentemp = (Integer) v1.get(i);
                        byte[] arraytt = (byte[]) ((byte[]) v.get(i));
                        System.arraycopy(arraytt, 0, arraytemp, index, lentemp);
                        index += lentemp;
                    }

                    return new String(arraytemp);
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }

            return "";
        }
    }

    public static String fromClob(Clob clob) {
        if (clob == null) {
            return "";
        } else {
            String resultString = "";

            try {
                Reader reader = clob.getCharacterStream();
                BufferedReader br = new BufferedReader(reader);
                String s = br.readLine();

                StringBuffer sb;
                for (sb = new StringBuffer(); s != null; s = br.readLine()) {
                    sb.append(s);
                }

                resultString = sb.toString();
            } catch (SQLException var6) {
                var6.printStackTrace();
            } catch (IOException var7) {
                var7.printStackTrace();
            }

            return resultString;
        }
    }

    public static String[] split(String str, String spilter) {
        if (str == null) {
            return null;
        } else if (spilter != null && !spilter.equals("") && str.length() >= spilter.length()) {
            ArrayList<String> al = new ArrayList();
            char[] cs = str.toCharArray();
            char[] ss = spilter.toCharArray();
            int length = spilter.length();
            int lastIndex = 0;
            int i = 0;

            while (i <= str.length() - length) {
                boolean notSuit = false;

                for (int j = 0; j < length; ++j) {
                    if (cs[i + j] != ss[j]) {
                        notSuit = true;
                        break;
                    }
                }

                if (!notSuit) {
                    al.add(str.substring(lastIndex, i));
                    i += length;
                    lastIndex = i;
                } else {
                    ++i;
                }
            }

            if (lastIndex <= str.length()) {
                al.add(str.substring(lastIndex, str.length()));
            }

            String[] t = new String[al.size()];

            for (int k = 0; k < al.size(); ++k) {
                t[k] = (String) al.get(k);
            }

            return t;
        } else {
            String[] t = new String[]{str};
            return t;
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String getStr(long src) {
        return String.valueOf(src);
    }

    public static boolean isNullOrWhiteSpace(String str) {
        return trimAllWhitespace(getStr(str)).length() == 0;
    }

    public static void main(String[] args) {
        String s = null;
        System.out.println(isNullOrWhiteSpace((String) s));
    }

    public static Blob getBlobfromString(String str) {
        Blob blob = null;
        if (str != null && !"".equals(str)) {
            try {
                blob = new SerialBlob(str.getBytes("UTF-8"));
            } catch (SerialException var3) {
                var3.printStackTrace();
            } catch (UnsupportedEncodingException var4) {
                var4.printStackTrace();
            } catch (SQLException var5) {
                var5.printStackTrace();
            }
        }

        return blob;
    }

    public static Clob getClobfromString(String str) {
        Clob clob = null;
        if (str != null && !"".equals(str)) {
            try {
                clob = new SerialClob(str.toCharArray());
            } catch (SerialException var3) {
                var3.printStackTrace();
            } catch (SQLException var4) {
                var4.printStackTrace();
            }
        }

        return clob;
    }

    public static String getStrFromList(List<?> list) {
        String str = "";
        if (list != null && list.size() != 0) {
            String pCode = ",";
            str = getStrFromList(list, pCode);
            return str;
        } else {
            return str;
        }
    }

    public static String getStrFromList(List<?> list, String pCode) {
        String str = "";

        for (int i = 0; i < list.size(); ++i) {
            String s = getStr(list.get(i));
            if (i > 0) {
                str = str + pCode;
            }

            str = str + s;
        }

        return str;
    }

    public static String getLowerStr(String str) {
        return !isEmpty(str) ? str.toLowerCase() : "";
    }

    public static String getUpperStr(String str) {
        return !isEmpty(str) ? str.toUpperCase() : "";
    }

    public static String getWidthFixedStr(String string, int startIndex, int width, boolean htmlStr) {
        if (string != null && !string.equals("")) {
            if (startIndex < 0) {
                return "";
            } else if (width < 0) {
                return "";
            } else {
                char baseChar = 32;
                char topChar = 126;
                double tempWidth = 0.0D;
                int charIndex = 0;
                string = string.substring(startIndex);
                StringBuffer sb = new StringBuffer();
                string = string.replaceAll("&lt;", "<");
                string = string.replaceAll("&gt;", ">");
                string = string.replaceAll("&nbsp;", " ");

                for (string = string.replaceAll("&quot;", "\""); charIndex <= string.length() - 1 && tempWidth <= (double) (width - 1); ++charIndex) {
                    char c = string.charAt(charIndex);
                    if (c >= baseChar && c <= topChar) {
                        tempWidth += 0.5D;
                    } else {
                        ++tempWidth;
                    }

                    sb.append(c);
                }

                String resultStr = sb.toString();
                if (resultStr.length() < string.length()) {
                    sb.deleteCharAt(resultStr.length() - 1);
                    sb.append("...");
                    resultStr = sb.toString();
                }

                if (htmlStr) {
                    resultStr = resultStr.replaceAll("<", "&lt;");
                    resultStr = resultStr.replaceAll(">", "&gt;");
                    resultStr = resultStr.replaceAll(" ", "&nbsp;");
                    resultStr = resultStr.replaceAll("\"", "&quot;");
                }

                return resultStr;
            }
        } else {
            return "";
        }
    }

    public static String removeIfLast(String string, String n) {
        String re = string;
        if (string != null && string.endsWith(n)) {
            re = string.substring(0, string.length() - n.length());
        }

        return re;
    }

    public static String removeIfFirst(String string, String n) {
        String re = string;
        if (string != null && string.startsWith(n)) {
            re = string.substring(n.length(), string.length());
        }

        return re;
    }

    public static String addQuots(String ids) {
        if (isEmpty(ids)) {
            return "''";
        } else {
            String result = "";
            result = removeIfFirst(ids, ",");
            result = removeIfLast(result, ",");
            result = result.replaceAll(",", "','");
            result = "'" + result + "'";
            return result;
        }
    }

    public static <T extends Number> List<T> makeNumberListFromString(String str, String splitChar, Class<T> elementType) {
        if (!StringUtils.hasLength(str)) {
            return null;
        } else {
            List<T> list = new ArrayList();
            String[] array = delimitedListToStringArray(str, splitChar);
            String[] var5 = array;
            int var6 = array.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                String element = var5[var7];
                list.add(NumberUtils.parseNumber(element, elementType));
            }

            return list;
        }
    }

    public static List<String> makeListFromString(String str, String splitChar, Class<String> elementType) {
        if (!StringUtils.hasLength(str)) {
            return null;
        } else {
            List<String> list = new ArrayList();
            String[] array = delimitedListToStringArray(str, splitChar);
            String[] var5 = array;
            int var6 = array.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                String element = var5[var7];
                list.add(element);
            }

            return list;
        }
    }

    public static List<String> getMatcherList(String patternStr, String content) {
        List<String> list = new ArrayList();
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            list.add(matcher.group());
        }

        return list;
    }

    public static String getMatcherString(String patternStr, String content) {
        StringBuilder str = new StringBuilder();
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            str.append(matcher.group()).append(",");
        }

        return str.length() != 0 ? str.substring(0, str.length() - 1) : "";
    }

    public static List<String> getMatcherList(String patternStr, String content, int flag) {
        List<String> list = new ArrayList();
        Pattern pattern = Pattern.compile(patternStr, flag);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            list.add(matcher.group());
        }

        return list;
    }

    public static String convertUndefineToNull(String source) {
        return isEmpty(source) ? source : source.replace("undefined", "null");
    }

    public static boolean isNumber(String s) {
        Pattern numberPattern = Pattern.compile("[0-9]*");
        Matcher matcher = numberPattern.matcher(s);
        return matcher.matches();
    }

    public static String connectArray(String[] stringArray) {
        if (stringArray != null && stringArray.length != 0) {
            if (stringArray.length == 1 && isEmpty(stringArray[0])) {
                return null;
            } else {
                StringBuilder b = new StringBuilder();
                int i = 0;

                while (true) {
                    b.append(stringArray[i]);
                    if (i == stringArray.length - 1) {
                        return b.toString();
                    }

                    b.append(",");
                    ++i;
                }
            }
        } else {
            return null;
        }
    }

    public static String connectList(List list) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        } else {
            StringBuilder b = new StringBuilder();
            int i = 0;

            while (true) {
                b.append(list.get(i));
                if (i == list.size() - 1) {
                    return b.toString();
                }

                b.append(",");
                ++i;
            }
        }
    }

    public static String connectSet(Set<String> set) {
        if (CollectionUtils.isEmpty(set)) {
            return "";
        } else {
            StringBuilder b = new StringBuilder();
            Iterator iterator = set.iterator();

            while (iterator.hasNext()) {
                b.append(iterator.next()).append(",");
            }

            return b.substring(0, b.length() - 1);
        }
    }

    public static boolean containsInStringArray(String src, String delimiter, String target) {
        if (!isEmpty(src) && !isEmpty(delimiter) && !isEmpty(target)) {
            String[] stringArray = delimitedListToStringArray(src, delimiter);
            String[] var4 = stringArray;
            int var5 = stringArray.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String srcString = var4[var6];
                if (target.equals(srcString)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static String ruleSplit(String strForSplit, String splitChr) {
        if (strForSplit != null && strForSplit.length() != 0 && splitChr != null && !splitChr.equals(" ") && splitChr.length() == 1) {
            String strSrc;
            for (strSrc = strForSplit.replace(" ", "").replace(splitChr, " ").trim(); strSrc.indexOf("  ") != -1; strSrc = strSrc.replace("  ", " ")) {
                ;
            }

            return strSrc.replace(" ", splitChr);
        } else {
            return strForSplit;
        }
    }

    public static String removeDuplicateString(String sourceValues) {
        StringBuilder sbResult = new StringBuilder();
        List<String> uniqueValue = new ArrayList();
        String[] vals = sourceValues.split(",");
        String[] var4 = vals;
        int var5 = vals.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String val = var4[var6];
            if (!uniqueValue.contains(val)) {
                uniqueValue.add(val);
                sbResult.append(sbResult.length() != 0 ? "," + val : val);
            }
        }

        return getStr(sbResult);
    }

    public static String addCharBefore(String sourceValues, int len, String chrAdd) {
        if (isNullOrWhiteSpace(sourceValues)) {
            return sourceValues;
        } else {
            while (sourceValues.length() < len) {
                sourceValues = chrAdd + sourceValues;
            }

            return sourceValues;
        }
    }

    public static String addCharEnd(String sourceValues, int len, String chrAdd) {
        if (isNullOrWhiteSpace(sourceValues)) {
            return sourceValues;
        } else {
            while (sourceValues.length() < len) {
                sourceValues = sourceValues + chrAdd;
            }

            return sourceValues;
        }
    }
}
