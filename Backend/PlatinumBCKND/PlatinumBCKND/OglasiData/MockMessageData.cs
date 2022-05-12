using PlatinumBCKND.Models;
using PlatinumBCKND.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockMessageData : IMessage
    {
        private OglasContext _messageCxt;

        public MockMessageData(OglasContext messageContext)
        {
            _messageCxt = messageContext;
        }
        public List<Message> AllMessages(string user)
        {
            var messages = _messageCxt.Messages.ToList();
            HashSet<string> HashSet = new HashSet<string>();
            List<Message> Messages = new List<Message>();
            messages.Reverse();
            foreach (Message Message in messages)
            {
                if (Message.messageFrom == user || Message.messageTo == user)
                {
                    string otherUser = Message.messageFrom == user ? Message.messageTo : Message.messageFrom;
                    if (!HashSet.Contains(otherUser))
                    {
                        Messages.Add(Message);
                        HashSet.Add(otherUser);
                    }
                }
            }
            Messages.Sort((y, x) => DateTime.Compare(x.date, y.date));

            return Messages;
        }


        public Message NewMessage(Message message)
        {

            message.ID = Guid.NewGuid();
            message.date = DateTime.Now;
            _messageCxt.Messages.Add(message);
            _messageCxt.SaveChanges();
            Console.WriteLine("MessDataQ -> NewMessage done");
            return message;

        }

        public List<Message> PrivateChat(string user1, string user2)
        {
            List<Message> messages = _messageCxt.Messages.ToList();
            List<Message> Messages = new List<Message>();
            Console.WriteLine("Provera da li ima poruka A:" + user1 + ", B:" + user2);
            foreach (var Message in messages)
            {
                if ((Message.messageFrom == user1 && Message.messageTo == user2) || (Message.messageFrom == user2 && Message.messageTo == user1))
                {
                    Messages.Add(Message);
                }
            }

            Messages.Sort((x, y) => DateTime.Compare(y.date, x.date));
            return Messages;
        }
    }
}
