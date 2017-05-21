package common;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

public class InstanceManager {
    private Stream<String> stream;

    public Instance readInstance(String instanceName) {
        Instance instance = new Instance();
        try{
            stream = Files.lines(Paths.get(instanceName));
            Iterator<String> iterator = stream.iterator();
            instance.setQtyItems(Integer.parseInt(iterator.next().toString()));
            instance.setBinCapacity(Integer.parseInt(iterator.next().toString()));
            
            while(iterator.hasNext()) {
                instance.getItems().add(Integer.parseInt(iterator.next().toString()));
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return instance;
    }
}
