public class ParImpar {
    public static void main(String[] args){
        int x=0;
        x = MyIO.readInt();
        while(x!=0){
            if(x%2==0){
                System.out.println("P\n");
            }else{
                System.out.println("I\n");
            }   
            x = MyIO.readInt();
        }
    }    
}
