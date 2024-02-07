class Triangulo{
    public static string IsTriangle(int a, int b, int c){
        if (a <= 0 || b <= 0 || c <= 0){
            return "n";
        }
        if (a + b <= c || a + c <= b || b + c <= a){
            return "n";
        }
        return null;
    }

    public static string ClassifyByAngle(int ladoA, int ladoB, int ladoC){
        int[] lados = new int[] { ladoA, ladoB, ladoC };
        Array.Sort(lados);
        int a = lados[0], b = lados[1], c = lados[2];

        if (c * c < a * a + b * b){
            return "a";
        } else if (c * c == a * a + b * b){
            return "r";
        } else{
            return "o";
        }
    }

    public static void Main(string[] args){
        int numTriangles = Convert.ToInt32(Console.ReadLine());
        for (int i = 0; i < numTriangles; i++){
            int a = Convert.ToInt32(Console.ReadLine());
            int b = Convert.ToInt32(Console.ReadLine());
            int c = Convert.ToInt32(Console.ReadLine());

            string triangle = IsTriangle(a, b, c);
            if (triangle == null)
            {
                triangle = ClassifyByAngle(a, b, c);
            }

            Console.WriteLine(triangle);
        }
    }
}
