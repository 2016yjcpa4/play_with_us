package com.github.yjcpaj4.play_with_us.util;

/**
 *
 * @author 차명도.
 * @see https://gist.github.com/gre/1650294
 * @see
 * https://github.com/alicelieutier/smoothScroll/blob/master/smoothscroll.js
 */
public enum TimingFunctions implements TimingFunction {

    Linear() {

        @Override
        public double func(double t) {
            return t;
        }
    },
    
    EaseInOutCubic() {

        @Override
        public double func(double t) {
            
            if (t < 0.5) {
                return 4 * t * t * t;
            }
            
            return (t - 1) * (2 * t - 2) * (2 * t - 2) + 1;
        }
    },
    ;
}

interface TimingFunction {

    double func(double t);
}
