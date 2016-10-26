package com.github.yjcpaj4.play_with_us.resource;

import java.io.File;

/**
 * 비디오 리소스를 관리하는 객체입니다.
 * 
 * ImageResource 나 SpriteImageResource 같이 관리하려고 하였지만
 * VLC 라이브러리를 사용하는 상태에서 파일객체만 던져주면 재생이 바로가능 하기에
 * 그냥 상수목록만 정리합니다.
 * 
 * @author 차명도.
 */
public class VideoResource {
    
    private VideoResource() {
        // 외부에서 생성자 호출 방지
    }

    public static final File MOV_INTRO = new File("./res/mov_intro.mp4");
    public static final File MOV_0001 = new File("./res/mov_00001.mp4");
}
