package com.business.intelligence.util;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extracter {

    /**
     * 使用正则表式进行匹配
     *
     * @param input
     *            字符串
     * @param regex
     *            正则表达式
     * @param index
     *            取匹配到的第几个项
     * @return 匹配到的第index个字符串
     */
    public static String match(String input, String regex, int index) {
        if (input == null || input.length() < 1) {
            return "";
        }
        if (regex == null || regex.length() < 1) {
            return "";
        }
        List<String> groups = match(input, regex);
        if (groups != null && groups.size() > index - 1) {
            return groups.get(index - 1);
        }
        return "";
    }

    /**
     * 使用正则表达式进行匹配
     *
     * @param input
     *            源字符串
     * @param regex
     *            正则表达式
     * @return 返回从 input 中匹配到的结果字符串
     */
    public static List<String> match(String input, String regex) {
        List<String> groups = new LinkedList<String>();
        if (input == null || input.length() < 1) {
            return groups;
        }
        if (regex == null || regex.length() < 1) {
            return groups;
        }
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(input);

        while (m.find()) {
            int count = m.groupCount();
            for (int i = 1; i <= count; i++) {
                String group = m.group(i);
                if (group != null && group.length() > 0) {
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    /**
     * 正则匹配，取前top个匹配项
     *
     * @param top
     *            指定匹配数量
     * @param input
     *            输入字符串
     * @param regex
     *            正则表达式
     * @return 匹配结果集
     */
    public static List<String> match(int top, String input, String regex) {
        List<String> groups = new LinkedList<String>();
        if (input == null || input.length() < 1) {
            return groups;
        }
        if (regex == null || regex.length() < 1) {
            return groups;
        }
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(input);
        int x = 0;
        while (m.find() && x < top) {
            int count = m.groupCount();
            for (int i = 1; i <= count; i++) {
                String group = m.group(i);
                if (group != null && group.length() > 0) {
                    groups.add(group);
                }
            }
            x++;
        }
        return groups;
    }

    /**
     * 获得第一个匹配项
     *
     * @param input
     *            输入字符串
     * @param regex
     *            正则表达式
     * @return 第一个匹配项
     */
    public static String matchFirst(String input, String regex) {
        if (input == null || input.length() < 1) {
            return "";
        }
        if (regex == null || regex.length() < 1) {
            return "";
        }
        String block = null;
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(input);
        int count = m.groupCount();
        if (m.find()) {
            for (int i = 1; i <= count; i++) {
                block = m.group(i);
                if (block != null && block.length() > 0) {
                    break;
                }
            }
        }
        return block == null ? "" : block;
    }

    public static boolean isMatch(String input, String regex) {
        if (input == null || input.length() < 1) {
            return false;
        }
        if (regex == null || regex.length() < 1) {
            return false;
        }
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(input);
        return m.find();
    }

    /**
     * 该方法用于
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 匹配
     *
     * @param html
     * @param match
     * @return
     */
    public static String match2(String html, String match) {
        int start = match.indexOf("<%");
        int end = match.indexOf("%>");
        String key = "";
        String startVal = match.substring(0, start);
        String endVal = match.substring(end + 2, match.length());
        Pattern pattern = Pattern.compile(startVal + ".*" + endVal);
        Matcher m = pattern.matcher(html);
        key = "";
        while (m.find()) {
            key = m.group();
            key = key.replaceAll(startVal, "").replaceAll(endVal, "");
            break;
        }
        return key;
    }

    public static void main(String[] args) {
        String input = "({1:2,2:3})";
        String regex="\\((.*)\\)";

        String block = null;
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(input);
        int count = m.groupCount();
        if (m.find()) {
            for (int i = 1; i <= count; i++) {
                block = m.group(i);
                if (block != null && block.length() > 0) {
                    break;
                }
            }
        }
        System.out.print(block);
    }

}
