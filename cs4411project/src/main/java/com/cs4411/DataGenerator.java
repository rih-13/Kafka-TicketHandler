package com.cs4411;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public interface DataGenerator<K>{
    public void loadNameTemplates(ArrayList<ArrayList<String>> nameParts);
    public void toggleNameTemplate(boolean useTemplate);
    public HashSet<K> generateDataset();

    public class UserDataGenerator implements DataGenerator{
        private HashMap<Integer, ArrayList<String>> nameParts;
        private boolean useNameTemplate;
        private long datasetSize;
        private String defaultName;
        private int numParts;
        private String sep;
        private static UserDataGenerator generator_instance = null;

        public static synchronized UserDataGenerator getUserDataGenerator(Long n){
            if (generator_instance == null){
                generator_instance = new UserDataGenerator(n);
                return generator_instance;
            }
            else{
                generator_instance.setDatasetSize(n);
                return generator_instance;
            }
        }

        public static synchronized UserDataGenerator getUserDataGenerator(){
            if (generator_instance == null){
                generator_instance = new UserDataGenerator();
                return generator_instance;
            }
            else{
                return generator_instance;
            }

        }

        private UserDataGenerator(){
            this.datasetSize = 0L;
            this.useNameTemplate = false;
            this.nameParts = null;
            this.defaultName = "user";
            this.sep = " ";

        }


        private UserDataGenerator(Long n){
           this.datasetSize = n;
           this.useNameTemplate = false;
           this.nameParts = null;
           this.defaultName = "user";
           this.sep = " ";
        }
        public void setDefaultName(String usrName){
            this.defaultName = usrName;
        }
        public void setSeparator(String sep){
            this.sep = sep;
        }
        public void setDatasetSize(Long n){
            this.datasetSize = n;
        }
        @Override
        public void loadNameTemplates(ArrayList nameParts) {
            if (nameParts.size() <= 0) return;
            if (!(nameParts.get(0) instanceof ArrayList<?>)) return;
            if (((ArrayList)nameParts.get(0)).size() <= 0) return;
            if (!(((ArrayList)((ArrayList)nameParts.get(0))).get(0) instanceof String)) return;
            this.numParts = 0;
            this.nameParts = new HashMap<Integer, ArrayList<String>>();
            for (int i = 0; i < nameParts.size(); i++){
                ArrayList nl = (ArrayList) nameParts.get(i);
                this.nameParts.put(i, nl);
                this.numParts += 1;
            }
        }

        @Override
        public void toggleNameTemplate(boolean useTemplate) {
            this.useNameTemplate = useTemplate;
        }

        public HashSet generateDataset(int seed){
            HashSet<User> userSet = new HashSet<>();
            Random r = new Random(seed);
            for (Long i = 0L; i < this.datasetSize; i++){
                String newName = "";
                if (this.useNameTemplate) {
                    for (int j = 0; j < this.numParts; j++) {
                        newName += (this.nameParts.get(i)).get(r.nextInt());
                        if (j < this.numParts - 1) {
                            newName += sep;
                        }
                    }
                }
                else{
                    newName = this.defaultName + i;
                }
                userSet.add(new User(i, newName));
            }

            return userSet;
        }

        @Override
        public HashSet generateDataset() {
            return generateDataset(0);
        }
    }

    public class TicketDataGenerator implements DataGenerator{
        private long datasetSize;
        private static TicketDataGenerator generator_instance = null;

        public static synchronized TicketDataGenerator getTicketDataGenerator(long n){
            if (generator_instance == null){
                generator_instance = new TicketDataGenerator(n);
                return generator_instance;
            }
            else{
                generator_instance.setDatasetSize(n);
                return generator_instance;
            }
        }

        public static synchronized TicketDataGenerator getUserDataGenerator(){
            if (generator_instance == null){
                generator_instance = new TicketDataGenerator();
                return generator_instance;
            }
            else{
                return generator_instance;
            }

        }

        private TicketDataGenerator(){
            this.datasetSize = 0L;

        }

        private TicketDataGenerator(Long n){
            this.datasetSize = n;
        }

        public void setDatasetSize(Long n){
            this.datasetSize = n;
        }
        @Override
        public void loadNameTemplates(ArrayList nameParts) {
            System.out.println("Method not supported.");
            return;
        }

        @Override
        public void toggleNameTemplate(boolean useTemplate) {
            System.out.println("Method not supported.");
            return;
        }

        @Override
        public HashSet generateDataset() {
            HashSet<Ticket> ticketSet = new HashSet<>();
            for (long i = 0L; i < this.datasetSize; i++){
                ticketSet.add(new Ticket(i));
            }
            return ticketSet;
        }
    }


}
