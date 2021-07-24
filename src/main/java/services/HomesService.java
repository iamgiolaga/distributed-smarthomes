package services;

import beans.Home;
import beans.Statistics;
import singleton.Singleton_Homes;
import singleton.Singleton_Statistics;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;

@Path("homes")
public class HomesService {

    @GET
    @Produces("application/json")
    public Response getHomes() {

        Singleton_Homes instance = Singleton_Homes.getInstance();
        return Response.ok( instance.getHomesList() ).build();

    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response insertHome(Home home) {

        Singleton_Homes instance = Singleton_Homes.getInstance();
        Singleton_Statistics statisticsInstance = Singleton_Statistics.getInstance();
        HashMap<Integer, ArrayList<Statistics>> localStatsMapUpdate;

        HashMap<Integer, Home> homesList = instance.addHome(home);

        if (homesList != null) {
            // SE L'AGGIUNTA E' ANDATA A BUON FINE INIZIALIZZO ANCHE LE SUE STATISTICHE //
            localStatsMapUpdate = statisticsInstance.getLocalList();
            localStatsMapUpdate.put(home.getId(), new ArrayList<>());
            statisticsInstance.setLocalList(localStatsMapUpdate);

            return Response.ok( instance.getHomesList() ).build();
        }

        else
            return Response.status(Response.Status.CONFLICT).build();

    }

    @Path("{home_id}")
    @DELETE
    @Produces("text/plain")
    public Response deleteHome(@PathParam("home_id") String home_id) {

        int home;

        try {
            home = Integer.parseInt(home_id);
        }

        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Singleton_Homes instance = Singleton_Homes.getInstance();

        if ( instance.removeHome(home) )
            return Response.ok().build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();

    }

}
