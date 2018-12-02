package main.services;

import main.model.FriendInfo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Consumer;

@CrossOrigin(origins = "*")
@RestController
public class ChatBotService {
    /**
     * store all the friends id.
     */
    private List<FriendInfo> friendsList = new ArrayList<FriendInfo>();

    /**
     * constructor
     */
    public ChatBotService() {
        for (int i=0; i<3; i++) {
            FriendInfo friend = new FriendInfo();
            friend.setName(": robot-"+i);
            friend.setOnline(false);
            friend.setId(""+(new Date()).getTime()+"-"+i);
            this.friendsList.add(friend);
        }
    }

    /**
     * Get online friends
     */
    @RequestMapping("/onlineFriends")
    public List<FriendInfo> getOnlineFriends(@RequestParam(value="chatterId", defaultValue="") String chatterId) {
        int randomSeed = (int) (Math.random()*10);
        int onlineFriendIndex = randomSeed%this.friendsList.size();
        this.friendsList.forEach(new Consumer<FriendInfo>() {

            @Override
            public void accept(FriendInfo friendInfo) {
                int pos = ChatBotService.this.friendsList.indexOf(friendInfo);
                if (pos == onlineFriendIndex) {
                    friendInfo.setOnline(true);
                } else {
                    friendInfo.setOnline(false);
                }
            }
        });

        return this.friendsList;
    }

    /**
     * Read the user messages
     */
    @RequestMapping("/messages")
    public Map<String, List<String>> messages(@RequestParam(value="chatterId", defaultValue="") String chatterId) {
        try {
            Map<String, List<String>> messages = new HashMap<String, List<String>>();
            for (int i = 0; i < 5; i++) {
                int chooseBase = (int) (Math.random()*999);
                int position = chooseBase % this.friendsList.size();
                FriendInfo friend = this.friendsList.get(position);
                List<String> messagesFromFriend = messages.get(friend.getId());
                if (messagesFromFriend != null) {
                    messagesFromFriend.add("I am fine, thank you. "+ chatterId);
                }
                else {
                    messagesFromFriend = new ArrayList<String>();
                    messagesFromFriend.add("I have seen you yesterday in Botany downs.");
                    messages.put(friend.getId(), messagesFromFriend);
                }
            }
            return messages;
        }
        catch (Exception e) {
            return null;
        }
    }
}
