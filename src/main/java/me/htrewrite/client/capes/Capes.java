package me.htrewrite.client.capes;

import me.htrewrite.client.capes.obj.CapeObj;
import me.htrewrite.client.capes.obj.UserCapeObj;
import me.htrewrite.client.capes.obj.UserObj;
import me.htrewrite.client.util.PostRequest;
import net.minecraft.client.resources.FolderResourcePack;

import java.util.ArrayList;
import java.util.HashMap;

public class Capes {
    private HashMap<Integer, CapeObj> capes;
    private HashMap<Integer, UserObj> users;
    private ArrayList<UserCapeObj> user_capes;

    public HashMap<String, CapeObj> wCapes;

    public Capes() {
        this.capes = new HashMap<>();

        String capesResponse = PostRequest.urlEncodedPostRequest("https://aurahardware.eu/api/cape/list_capes.php", "");
        String[] capesSplit = capesResponse.split("]");
        for(String line : capesSplit) {
            String[] row = line.split(";");
            capes.put(Integer.parseInt(row[0]), new CapeObj(Integer.parseInt(row[0]), row[1], row[2]));
        }

        this.users = new HashMap<>();

        String usersResponse = PostRequest.urlEncodedPostRequest("https://aurahardware.eu/api/user/get_user_ids.php", "");
        if(!usersResponse.contentEquals("")) {
            String[] userLines = usersResponse.split("]");
            for (String line : userLines) {
                String[] row = line.split(";");
                users.put(Integer.parseInt(row[0]), new UserObj(Integer.parseInt(row[0]), row[1], Integer.parseInt(row[2])));
            }
        }

        this.user_capes = new ArrayList<>();

        String userCapesResponse = PostRequest.urlEncodedPostRequest("https://aurahardware.eu/api/cape/user_capes.php", "");
        if(!userCapesResponse.contentEquals("")) {
            String[] user_capes_split;
            if(userCapesResponse.contentEquals("]"))
                user_capes_split = userCapesResponse.split("]");
            else user_capes_split = new String[]{userCapesResponse};

            for(String line : user_capes_split) {
                String[] row = line.split(";");
                user_capes.add(new UserCapeObj(Integer.parseInt(row[0]), row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3])));
            }
        }

        this.wCapes = new HashMap<>();
        for(UserCapeObj userCapeObj : user_capes) {
            UserObj userObj = users.get(userCapeObj.user_id);
            if(userCapeObj.id == userObj.cape_id)
                wCapes.put(userCapeObj.player, capes.get(userObj.id));
        }
    }
}