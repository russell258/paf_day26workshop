package sg.edu.nus.iss.paf_day26workshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private String cId;
    private String user;
    private Integer rating;
    private String text;
    private Integer gid;
}
