package sample;

/**
* Created by Никита on 14.04.2017.
*/
public class Article {
   private String url;
   private String name;
   private String date;

   public Article(String url, String name, String date) {
       this.url = url;
       this.name = name;
       this.date = date;
   }

   public String getDate() {
       return date;
   }

   public void setDate(String date) {
       this.date = date;
   }

   public String getUrl() {
       return url;
   }

   public void setUrl(String url) {
       this.url = url;
   }

   public String getName() {
       return name;
   }

   public void setName(String name) {
       this.name = name;
   }

   @Override
   public String toString() {
       return "Article{" +
               "url='" + url + '\'' +
               ", name='" + name + '\'' +
               ", date='" + date + '\'' +
               '}';
   }
}
