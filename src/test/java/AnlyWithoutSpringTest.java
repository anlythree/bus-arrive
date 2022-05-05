import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;

public class AnlyWithoutSpringTest {
    @Test
    public void test1(){
        ArrayList<String> strings = Lists.newArrayList("first", "second", "third","fourth");
        strings.stream().peek(string->{
            if("second".equals(string)){
                strings.add(string);
            }
        });
        System.out.println(strings);
    }
}
