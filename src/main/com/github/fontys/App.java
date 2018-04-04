package com.github.fontys;


import com.github.fontys.base.CORSFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;


/*
 * This Java source file was generated by the Gradle 'init' task.
 */
@ApplicationPath("/api")
public class App extends ResourceConfig
{
    public App() {
        packages("com.github.fontys");
        register(MultiPartFeature.class);
        register(CORSFilter.class);
    }

    /*
    public App(){
        register(MultiPartFeature.class);
        packages("com.github.fontys.beans");
       // register(new LoggingFeature(Logger.getLogger(App.class.getName()), LoggingFeature.Verbosity.PAYLOAD_ANY));
    }*/

    /*
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        // register resources and features
        classes.add(MultiPartFeature.class);
        return classes;
    }*/

}
