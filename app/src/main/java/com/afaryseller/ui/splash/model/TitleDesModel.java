package com.afaryseller.ui.splash.model;

import com.afaryseller.ui.selectskills.model.SkillModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TitleDesModel {
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("heading")
        @Expose
        private String heading;

        @SerializedName("heading_fr")
        @Expose
        private String headingFr;

        @SerializedName("description")
        @Expose
        private String description;

        @SerializedName("description_fr")
        @Expose
        private String descriptionFr;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHeading() {
            return heading;
        }

        public void setHeading(String heading) {
            this.heading = heading;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getHeadingFr() {
            return headingFr;
        }

        public void setHeadingFr(String headingFr) {
            this.headingFr = headingFr;
        }

        public String getDescriptionFr() {
            return descriptionFr;
        }

        public void setDescriptionFr(String descriptionFr) {
            this.descriptionFr = descriptionFr;
        }
    }
}
