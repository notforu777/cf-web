package ru.itmo.wp.form;

import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.Tag;

import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

public class PostCredentials {
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 60)
    private String title;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 65000)
    @Lob
    private String text;

    @NotEmpty
    @Size(max = 1000)
    @Pattern(regexp = "([a-z]+[\\s]*)*", message = "Only lower Latin and whitespaces")
    private String tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Post toPost() {
        Post ans = new Post();
        ans.setText(this.getText());
        ans.setTitle(this.getTitle());

        Set<String> temp = new HashSet<>(Arrays.asList(this.getTags().trim().split("\\s+")));
        List<Tag> res = new ArrayList<>();
        for (String it : temp) {
            Tag n = new Tag();
            n.setName(it);
            res.add(n);
        }

        ans.setTags(res);
        return ans;
    }
}