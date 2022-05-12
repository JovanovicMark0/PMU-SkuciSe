
using AutoMapper;
using PlatinumBCKND.Models;
using PlatinumBCKND.ViewModels;
using PlatinumBCKND.Interfaces;
using Microsoft.AspNetCore.SignalR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace PlatinumBCKND.Hubs
{
    public class ChatHub : Hub
    {
        public readonly static List<UserView> _Connections = new List<UserView>();
        private readonly static Dictionary<string, string> _ConnectionMap = new Dictionary<string, string>();

        private readonly OglasContext _context;
        //private readonly IMapper _mapper;
        private IMessage _message;

        public ChatHub(OglasContext context, IMessage message) //, IMessage message
        {
            _context = context;
            //_mapper = mapper;
            _message = message;
        }


        //SLANJE PORUKE

        public async Task SendMessage(string from, string to, string message)
        {
            Console.Write("1: MESSAGE ARRIVED: " + from + " -> " + to + " : " + message + " \n");
            //if (_ConnectionMap.TryGetValue(to, out string userID))
            // {
            Console.Write("2 \n");
            //var sender = _Connections.Where(u => u.UserEmail == IdentityName).First(); 

            if (!string.IsNullOrEmpty(message.Trim()))
            {
                Console.Write("3 \n");
                var messageViewModel = new MessageView()
                {
                    Content = Regex.Replace(message, @"(?i)<(?!img|a|/a|/img).*?>", string.Empty),
                    From = from,
                    To = to,
                    Time = DateTime.Now.ToLongDateString()
                };

                _message.NewMessage(new Message
                {
                    messageFrom = from,
                    messageTo = to,
                    messageContent = message
                });

                Console.Write("4 \n");
                await Clients.Client(to).SendAsync("newMessage", from, to, messageViewModel.Content, messageViewModel.Time);
                await Clients.Caller.SendAsync("newMessage", from, to, messageViewModel.Content, messageViewModel.Time);
            }
            //}
            /*_message.NewMessage(new Message
            {
                messageFrom = from,
                messageTo = to,
                messageContent = message
            });

            await Clients.All.SendAsync("newMessage", from, to, message, DateTime.Now);*/
        }

        //Identity Name
        private string IdentityName
        {
            get { return Context.User.Identity.Name; }
        }

    }
}
