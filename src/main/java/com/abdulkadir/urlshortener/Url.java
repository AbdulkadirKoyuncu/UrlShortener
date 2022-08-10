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

    public Url() {
    }

    public Url(String id, String pureUrl, String hashUrl) {
        this.id = id;
        this.pureUrl = pureUrl;
        this.hashUrl = hashUrl;
    }

}
