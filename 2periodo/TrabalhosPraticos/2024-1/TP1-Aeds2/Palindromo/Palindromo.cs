using System;

namespace Palindromo
{
    public class Palindromo{
        static bool isFim(string a){
            return a[0] == 'F' && a[1] == 'I' && a[2] == 'M';
        }
        static bool isPalindromo(string a){
            bool ehPalindromo = true;
            int len = a.Length;
            for(int i=0;i<len && ehPalindromo;i++){
                if(a[i]!=a[len-i-1]){
                    ehPalindromo = false;
                }
            }
            return ehPalindromo;
        }
        static void Main(string[] args){
            string str = "";
            str = Console.ReadLine();
            while (!isFim(str)){
                if (isPalindromo(str)) Console.WriteLine("SIM");
                else Console.WriteLine("NAO");
                str = Console.ReadLine();
            }
        }
    }
}

