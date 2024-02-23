using System;

namespace IdadeDonaMonica
{
    class Idade
    {
        public static int filhoMaisVelho(int M, int A, int B) {
            int filho = M - A - B;
            return filho;
        }
        static void Main(string[] args) {
            int M, A, B;
            do {
                M = Convert.ToInt32(Console.ReadLine());
                A = Convert.ToInt32(Console.ReadLine());
                B = Convert.ToInt32(Console.ReadLine());
            }while ((M < 40 && M > 110) && (A < 1 && A >= M) && (B < 1 && B >= M) && (A == B));
            Console.WriteLine(filhoMaisVelho(M, A, B));

        }
    }
}