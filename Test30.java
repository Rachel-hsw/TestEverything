import java.io.*;
public class Test30 {
    public static void main(String[] args) {
        try {
            System.out.println(new File(".").getAbsolutePath());
            FileInputStream fis=new FileInputStream("Test30.java");
            InputStreamReader dis=new InputStreamReader(fis);
            BufferedReader reader=new BufferedReader(dis);
            String s;
            while ((s=reader.readLine())!=null){
                System.out.println("read:"+s);
            }
            dis.close();
        }catch (IOException e){
            System.out.println(e);
        }
    }
}
