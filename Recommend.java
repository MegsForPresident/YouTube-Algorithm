import java.awt.Desktop;
import java.io.*;
import java.util.*;



public class Recommend{
    private static Scanner scan = new Scanner(System.in);
    private static LinkedList<String> files = new LinkedList<>();
    private static LinkedList<String> later = new LinkedList<>();
    private static LinkedList<String> recent = new LinkedList<>();
    private static LinkedList<String> watched = new LinkedList<>();
    private static LinkedList<String> dislikes = new LinkedList<>();
    private static LinkedList<String> likes = new LinkedList<>();
    private static LinkedList<String> dislikeTags = new LinkedList<>();
    private static LinkedList<String> likeTags = new LinkedList<>();
    private static HashMap<String, LinkedList<String>> tags = new HashMap<>();
    private static Random random = new Random();//true
    public static void main(String[] args)throws IOException{
        HashMap<String,LinkedList<String>> map = List();
        HashMap<String,LinkedList<String>> tagMap = reMap(map, files);
        algorithm(map,tagMap);
        System.out.println(evaluate());
    }
    private static String evaluate() {
        HashMap<String,Integer> like = new HashMap<>();
        //likeTags dislikeTags
        LinkedList<String> s = new LinkedList<>();
        for(String h : likeTags){
            if(s.contains(h)){
                like.put(h,like.get(h) + 1);
            }
            else{
                like.put(h,1);
                s.add(h);
            }
        }
        int size = likeTags.size();
        String evaluation = "";
        for(String f : like.keySet()){
            evaluation += "\n"+f+" = "+ ((like.get(f) * 100) / size) + "%";
        }
        System.out.println(like);
        return evaluation.replaceFirst("\n", "");
    }
    private static void algorithm(HashMap<String, LinkedList<String>> map,HashMap<String, LinkedList<String>> tagMap)throws IOException {
        String video = recommend(files,likeTags,tagMap,map);
        boolean play = true;
        algorithm:
        while(true){    
            String response = ask("Do you want to see " + video.replace("//watch","")+": ").toLowerCase().trim();
            if(video.contains("//watch")){
                if(ask("Remove from Watch Later").contains("yes")){
                    later.remove(video.replace("//watch",""));
                }
            }
            if(response.contentEquals("test")){
                play = !play;
            }
            if(response.contentEquals("yes")){
               try {
                if(play){    
                    Desktop.getDesktop().open(new File(map.get(video).get(2)));
                }
                String review = !watched.contains(video) ? ask("Did you like it: "):"yes";
                if(review.contentEquals("yes") && (!likes.contains(video))){
                    likeTags.addAll(asList(map.get(video).get(1)));
                    likes.add(video);
                }
                else{
                    dislikes.add(video);
                    dislikeTags.addAll(asList(map.get(video).get(1)));
                }
                recent.add(video);
                watched.add(video);
                }
                catch(Exception e){
                    System.out.println("You don't have that movie. If you do then change the path of the tag");
                    dislikes.add(video);
                }
            }
            else if(response.contentEquals("don't recommend")){
                dislikes.addAll(movies(map.get(video).get(0),map,files));
                dislikeTags.addAll(asList(map.get(video).get(1)));
            }
            else if(response.contentEquals("don't")){
                System.out.println("NEver");
                dislikes.add(video);
                dislikeTags.addAll(asList(map.get(video).get(1)));
            }
            else if(response.contentEquals("watch later") || response.contains("later")){
                later.add(video);
            }
            else if(response.contentEquals("break")){
                break algorithm;
            }
            else{
                recent.add(video);
            }
            video = recommend(files,likeTags,tagMap,map);
            if(recent.size() >= "abcde".length()){
                recent.pop();
            }
            dislikeTags = common(dislikeTags,likeTags);
            System.out.println("Like : "+likeTags + "\nDislikes"+dislikeTags + "\nWatch Later : "+later);
        }    
    }
    private static LinkedList<String> movies(String director,HashMap<String,LinkedList<String>> map,LinkedList<String> files) {
        LinkedList<String> movies = new LinkedList<>();
        for(String file : files){
            file = removeExtension(file);
            if(map.get(file).contains(director)){
                movies.add(file);
            }
        }
        return movies; 
    }
    private static HashMap<String,LinkedList<String>> reMap(HashMap<String,LinkedList<String>>map,LinkedList<String>files){
        HashMap<String,LinkedList<String>>map2 = new HashMap<>();
        for(String file : files){
            LinkedList<String>tags2 = asList(map.get(removeExtension(file)).get(1));
            for(String tag : tags2){
                if(!map2.keySet().contains(tag)){
                    LinkedList<String>tagsForMovies = new LinkedList<>();
                    for(String video : files){
                        video = removeExtension(video);
                        if(map.get(video).get(1).contains(tag)){
                            tagsForMovies.add(video);
                        }
                    }
                    map2.put(tag,tagsForMovies);
                }   
            }
        }
        return map2;
    }
    private static String recommend(LinkedList<String> files2,LinkedList<String>tags,HashMap<String, LinkedList<String>>tagMap,HashMap<String, LinkedList<String>>map ) {
        String video = removeExtension(files.get(random.nextInt(files.size())));
        while(recent.contains(video) || dislikes.contains(video)){
            video = removeExtension(files.get(random.nextInt(files.size())));
            LinkedList<String>tagss = asList(map.get(video).get(1));
            if(!(later.size() <= 0) && getRandomBoolean(getRandomBoolean(1) ? 3 : 5)){
                System.out.println("Watch Later");
                int g = random.nextInt(later.size()-1);
                System.out.println(g);
                return later.get(g <= 0 ? 0 : g)  + "//watch";
            }
            if(getRandomBoolean(1)){
                System.out.println("\nlikess\n");
                try{
                    while(!(recent.contains(video) && watched.contains(video))){
                        video = likes.get(modulus(random.nextInt(likes.size())));
                        if(files.contains(video)){
                            return video;
                        }
                    }
                }                                                                                                                                    
                catch(Exception e){
                    video = likes.get(0);
                        if(files.contains(video)){
                            return video;
                        }
                }
            }
            if(getRandomBoolean(5) &&!(tags.size() <= 1)){
                System.out.println("tagged\n");
                String videod = video;
                try{    
                    video = tagMap.get(tags.get(random.nextInt(tags.size()))).get(random.nextInt(tagMap.get(tags.get(random.nextInt(tags.size()-1))).size()));
                    while(dislikes.contains(video) && recent.contains(video)){
                        video = tagMap.get(tags.get(random.nextInt(tags.size()))).get(random.nextInt(tagMap.get(tags.get(random.nextInt(tags.size()-1))).size()));
                    }
                }catch(Exception e){
                    return videod;
                }
                return video;
            }
            if(!contains(tagss,dislikeTags) && !(recent.contains(video) || dislikes.contains(video))){
                return video;
            }
         }
        return video;
    }

    private static int modulus(int nextInt) {
        if(nextInt < 0){
            return nextInt * -1;
        }
        return nextInt;
    }
    private static boolean contains(LinkedList<String> tagss, LinkedList<String> dislikeTags2) {
        for(String t : tagss){
            if(!dislikeTags2.contains(t)){
                return true;
            }
        }
        return false;
    }
    private static LinkedList<String> common(LinkedList<String>list1,LinkedList<String>list2){
        LinkedList<String>list3 = new LinkedList<>();
        for(String s : list1){
            if(!list2.contains(s)){
                list3.add(s);
            }
        }
        return list3;
    }
    private static boolean getRandomBoolean(int p){
        return random.nextInt(p) == p;
    }
    private static HashMap<String,LinkedList<String>> List() throws FileNotFoundException{
        File folder = new File("C:\\Users\\sudip\\Tags");
        HashMap<String,LinkedList<String>>map = new HashMap<>();
        for(String file : folder.list()){
            LinkedList<String>data = getData("C:\\Users\\sudip\\Tags\\" + file);
            files.add(file.replaceAll("[{}]", ""));
            map.put(removeExtension(file),data);
            tags.put(removeExtension(file),asList(data.get(1)));
        }
        return map;
        
    }
    private static LinkedList<String> asList(String s){
        LinkedList<String>d = new LinkedList<>();
        for(String f : s.split(", ")){
            d.add(f);
        }
        return d;
    }
    private static LinkedList<String> getData(String file) throws FileNotFoundException{
        LinkedList<String> list = new LinkedList<>();
        Scanner reader = new Scanner(new File(file));
        String[] names = {"Creater : ","Tags : ","Path : "};
        int c = 0;
        while(reader.hasNextLine()){
            String g = reader.nextLine();
            if(g.contains("\\")){
                list.add(g.replace(names[c],""));
            }
            list.add(g.replaceAll("[{}]", "").replace(", $ultimatum","").replace(names[c++],"").toLowerCase());
        }
        return list;
    }
    private static String ask(String query){
        System.out.print(query);
        return scan.nextLine();
    }
    private static String removeExtension(String file) {
        return file.replace(file.substring(file.lastIndexOf(".")),"");
    }
}