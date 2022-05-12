using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Threading.Tasks;

namespace PlatinumBCKND.Utils
{
    public class Common
    {
        //random salt string

        public static byte[] GetRandomSalt(int length)
        {
            var random = new RNGCryptoServiceProvider();
            byte[] salt = new byte[length];
            random.GetNonZeroBytes(salt);
            return salt;
        }

        //create pass with salt
        public static byte[] SaltHashPassword(byte[] password, byte[] salt)
        {
            HashAlgorithm algorithm = new SHA256Managed();
            byte[] plainTextWithSaltBytes = new byte[password.Length + salt.Length];
            for(int i=0; i<salt.Length;i++)
            {
                plainTextWithSaltBytes[password.Length + i] = salt[i];
            }
            return algorithm.ComputeHash(plainTextWithSaltBytes);
        }
    }
}
