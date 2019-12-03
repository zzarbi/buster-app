package busterapp.index;

import spark.*;

public class IndexController {

    public static Route notFound = (Request request, Response response) -> {
        
        return "{\"code\":400}";
    };
}