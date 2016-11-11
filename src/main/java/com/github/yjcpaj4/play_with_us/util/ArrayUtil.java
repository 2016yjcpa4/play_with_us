package com.github.yjcpaj4.play_with_us.util;

public class ArrayUtil {
    
    
    /**
     * 
     * 배열 인덱스가 음수값 혹은 범위 밖의 인덱스일경우 보정시켜줍니다.
     * 
     * @param n 배열의 인덱스
     * @param c 배열의 크기
     * @return 보정된 인덱스값
     */
    public static int getFixedArrayIndex(int n, int c) {
        return n < 0 ? n % c + c : n % c;
    }
}
