import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/*
 * This Class provide by the System, 
 */

public class SystemRun 
{
	
	public static void main(String[] args) 
	{
		FileInputStream inputFile;
		Properties prop = new Properties();
		String projectclassname;
		
		try 
		{
            inputFile = new FileInputStream("ProjectProperties.properties");
            prop.load(inputFile);
            inputFile.close();
	        projectclassname = prop.getProperty("ProjectClassName");
	    	Class<?> Project = Class.forName(projectclassname);	    	
	    	Constructor<?> localConstructor = Project.getConstructor(new Class[0]);
	    	ComputingSystem<?, ?, ?, ?> realproject = (ComputingSystem<?, ?, ?, ?>) localConstructor.newInstance(new Object[0]);
	    	realproject.Run();
				
	    } catch (InstantiationException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e) {
		    e.printStackTrace();
	    } catch (FileNotFoundException ex) {
            System.out.println("error, no property file existing");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("load error");
            ex.printStackTrace();
        }
	  
  	
    }
}