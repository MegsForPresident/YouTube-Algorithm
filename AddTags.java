import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
public class AddTags{
    private static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        String path = ask("File: ");
        tagWriter(path);
        scan.close();
    }
    private static void tagWriter(String path) throws IOException{
        File file = new File(path);
        int n = 0;
        if(file.exists()){
            if(file.isFile()){
                String name = /*fix*/(removeExtenstion(getFileName(path)));
                System.out.println(name);
                String tags = ask("Enter a Tag:");
                if(!tags.contentEquals("skip")){               
                    while(!tags.endsWith("$ultimatum")){
                        tags += ", "+ask("Enter a another Tag(type $ultimatum to end):").toLowerCase();
                    }
                    String creater = ask("Director: ");
                    boolean isNameOkay = ask("Is the name okay " + name+": ").toLowerCase().contentEquals("yes");
                    if(!isNameOkay){
                        name = ask("Name: ");
                    }
                    File nfile = new File("C:\\Users\\sudip\\Tags" + "\\" + name + ".txt");
                    if(nfile.exists()){
                        nfile = new File("C:\\Users\\sudip\\Tags\\" + name + latest(name,"C:\\users\\sudip\\tags") + ".txt");
                    }
                    FileWriter writer = new FileWriter(nfile);
                    writer.write("Creater : " + creater + "\n");
                    writer.write("Tags : " + "{"+tags+"}\n");
                    writer.write("Path : " + path);
                    writer.close();
                }
            }
            else{
                if( n == 0){
                    for(String files : file.list()){
                        tagWriter(path + "\\" + files);
                    }
                    n++;
                }
            }
        }
        else{
            System.out.println("File does not exists");
        }
    }
    private static String fix(String removeExtenstion) {
        String[]s = removeExtenstion.split(" ");
        for(int c = 0;c < s.length;c++){
            String charIn = (s[c].charAt(0)+ "").toLowerCase();
            s[c] = s[c].toLowerCase().replace(charIn,charIn.toUpperCase());
        }
        return null;
    }
    private static String latest(String name,String path) {
        int n = 0;
        for(String s : new File(path).list()){
            if(s.toLowerCase().startsWith(name.toLowerCase())){
                n++;
            }
        }
        return " (" + n + ")";
    }
    private static String removeExtenstion(String fileName) {
        System.out.println(fileName);
        System.out.println(fileName.substring(fileName.lastIndexOf(".")));
        return fileName.replace(fileName.substring(fileName.lastIndexOf(".")),"");
    }
    private static String getFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("\\")).replace("\\","");
    }
    private static String ask(String ask){
        System.out.print(ask);
        return scan.nextLine();
    }
}