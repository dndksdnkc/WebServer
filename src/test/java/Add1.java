import Meavn.Add;
import org.junit.Test;

public class Add1 implements Add {

  @Test
    public  void x(){
        int a=1,b=2;
      int z=  add(a,b);
        System.out.println(z);
    }
    @Override
    public int add(int x, int y) {
      return x+y;
    }
}
