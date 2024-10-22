package com.c446.ironbound_artefacts.components;

import java.util.ArrayList;
import java.util.Arrays;

public record ClassLevelData(int[] maxLevel, String[] classes, String[] subClasses, String[] otherData) {
    protected static class StringArrayCodec {
        public String[] strings;
        public String representation;

        StringArrayCodec(String[] strings) {
            this.strings = strings;
            this.representation = Arrays.toString(strings);
        }

        public ArrayList<String> getFromRepresentation(String listAsString) {
            ArrayList<String> returned = new ArrayList<String>();
            StringBuilder builder = new StringBuilder();
            ArrayList<Character> banned = new ArrayList<>(){};
            banned.add('[');
            banned.add(']');
            banned.add(',');
            banned.add(' ');

            for (char c : listAsString.toCharArray()) {
                if (!banned.contains(c)) {
                    builder.append(c);
                } else {
                    returned.add(builder.toString());
                    builder.delete(0, builder.length());
                }
            }
            return returned;
        }

    }

    public static void main(String[] args) {
        var exampleCodec = new StringArrayCodec(new String[]{"A", "B", "C", "D"});
        System.out.println(exampleCodec.representation);
        exampleCodec.getFromRepresentation(exampleCodec.representation).forEach(System.out::println);
    }

}
