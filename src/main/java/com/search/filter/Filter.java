package com.search.filter;

import java.util.*;

public class Filter {

    public static ArrayList<String> search(List<String> data, String searchStr) {
        ArrayList<String> result = new ArrayList<>();
        int [] index = lowAndHigh(data, searchStr);
        if (index[0] == index[1]) {
            result.add(data.get(index[0]));
        }
        for (int i = index[0]; i < index[1]; i++) {
            result.add(data.get(i));
        }
        return result;
    }

    private static int[] lowAndHigh(List<String> data, String searchStr) {
        int [] lowAndHigh = new int[2];
        int low = 0, high = data.size() - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (data.get(mid).toLowerCase().compareTo(searchStr) > 0) {
                high = mid;
            }
            else {
                low = mid + 1;
            }
        }
        lowAndHigh[0] = low;
        high = data.size() - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (data.get(mid).toLowerCase().compareTo(searchStr) < 0) {
                low = mid;
            }
            else if (data.get(mid).toLowerCase().startsWith(searchStr)) {
                while (mid < data.size() - 1 && data.get(mid + 1).toLowerCase().startsWith(searchStr)) {
                    mid++;
                }
                lowAndHigh[1] = mid;
                return lowAndHigh;
            }
            else {
                high = mid - 1;
            }
        }
        return lowAndHigh;
    }
}
