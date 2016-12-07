package com.github.yjcpaj4.play_with_us.resource;

import java.io.File;

/**
 * 비디오 리소스를 관리하는 객체입니다.
 * 
 * SoundResource 나 SpriteResource 같이 관리하려고 하였지만
 * VLC 라이브러리를 사용하는 상태에서 파일객체만 던져주면 재생이 바로가능 하기에
 * 그냥 상수목록만 정리합니다.
 * 
 * @author 차명도.
 */
public class MovieResource {
    
    private MovieResource() {
        // 외부에서 생성자 호출 방지
    }

    public static final File MOV_INTRO = new File("res/mov.intro.mp4");
    public static final File MOV_GHOST = new File("res/mov.ghost.mp4");
    public static final File MOV_CREDIT = new File("res/mov.credit.mp4");
}
