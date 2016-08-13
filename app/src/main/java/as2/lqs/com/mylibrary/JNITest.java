/**
 * Created by Administrator on 2016/8/11.
 */
package as2.lqs.com.mylibrary;
public class JNITest {
    static{
        System.loadLibrary("MyLib");
    }
    public native String getStrFromJni();
}
