/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package random.apps;

import java.util.Arrays;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
public class App {
    static MongoCollection<Document> gradesCollection; 
    public static Scanner myObj; 
    public static void main(String[] args) {
        String uri = "mongodb+srv://vinnyvd2018:Thevinster123@crud.gljwjde.mongodb.net/?retryWrites=true&w=majority";

        try(MongoClient mongoClient = MongoClients.create(uri)){
            MongoDatabase database = mongoClient.getDatabase("CRUD_Operations");
            gradesCollection = database.getCollection("grader");

            welcomeScreen();
        }
    }
    public static void welcomeScreen(){
        myObj = new Scanner(System.in);
        System.out.println("What would you like to do? \n 1: Enter a new grade \n 2: Display all grades \n 3: Update a grade \n 4: Delete a grade \n 5: Quit");
        switch(myObj.nextInt()){
            case 1:
                insertGrade();
                break;
            case 2: 
                showGrades(false);
                break;
            case 3: 
                updateGrade();
                break;
            case 4:
                deleteGrade();
                break;
            case 5:
                System.exit(0);
        }
    }
    public static void deleteGrade(){
        showGrades(true);
        System.out.println("Enter the title that you would like to delete.");
        myObj.nextLine();
        try {
                // Deletes the first document that has a "title" value of "The Garbage Pail Kids Movie"
                gradesCollection.deleteOne(eq("title", myObj.nextLine()));
                
            // Prints a message if any exceptions occur during the operation
            } catch (MongoException me) {
                System.err.println("Unable to delete due to an error: " + me);
            }
        welcomeScreen();
    }
    public static void insertGrade(){
        System.out.println("Enter the assigments name.");
        myObj.nextLine();
        String aName = myObj.nextLine();
        System.out.println("Enter the assigment grade");
        String aGrade = myObj.nextLine();
        try {
                // Inserts a sample document describing a movie into the collection
                gradesCollection.insertOne(new Document()
                        .append("_id", new ObjectId())
                        .append("title", aName)
                        .append("grade", aGrade));
                // Prints the ID of the inserted document
                welcomeScreen();
            
            // Prints a message if any exceptions occur during the operation
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
    }
    public static void showGrades(boolean updating){
        Bson projectionFields = Projections.fields(
            Projections.include("title", "grade"),
            Projections.excludeId());
        // Retrieves documents that match the filter, applying a projection and a descending sort to the results
        MongoCursor<Document> cursor = gradesCollection.find()
            .projection(projectionFields)
            .sort(Sorts.ascending("title")).iterator();
            try {
                while(cursor.hasNext()) {
                    Document values = cursor.next();
                    System.out.println("Title: " + values.getString("title") + " | Grade: " + values.getString("grade"));
                }
            } finally {
                cursor.close();
            }
        if(!updating)
            welcomeScreen();
    }
    public static void updateGrade(){
        showGrades(true);
        System.out.println("Please enter the title of the grade you would like to change");
        myObj.nextLine();
        Document doc = gradesCollection.find(eq("title", myObj.nextLine()))
                .first();
        System.out.println("What would you like the new grade to be?");
        Bson updates = Updates.combine(
            Updates.set("grade", myObj.nextLine()));
        try {
                gradesCollection.updateOne(doc, updates, new UpdateOptions().upsert(false));
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        welcomeScreen();
    }
}
