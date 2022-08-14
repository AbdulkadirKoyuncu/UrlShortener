package com.abdulkadir.urlshortener;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "urls")
public class Url {

    @Id
    private String id;
    @Field(name="irfan")
    private String pureUrl;
    @Field(name="abdulkadir")
    private String hashUrl;
    @Field(name = "ClickCount")
    private int clickCount;

    public Url() {
    }

    public Url(String id, String pureUrl, String hashUrl, int clickCount) {
        this.id = id;
        this.pureUrl = pureUrl;
        this.hashUrl = hashUrl;
        this.clickCount=clickCount;
    }
}
