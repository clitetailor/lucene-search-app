import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mypackage.main.prototype.Site;

public class JsonSerializeTest {
    public static void main(String[] args) {
        Site site = new Site();
        site.title = "test title!";
        site.content = "sample content!";

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(site);

            System.out.println(json);
        } catch (JsonProcessingException e) {
            System.out.println(e.getClass() + " : " + e.getMessage());
        }
    }
}
