package services;

import beans.Statistics;
import main.java.beans.DevAndMean;
import singleton.Singleton_Statistics;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("statistics")
public class StatService {

    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public Response addGlobalStatistic(Statistics stat) {

        Singleton_Statistics instance = Singleton_Statistics.getInstance();

        if ( instance.addGlobalStat(stat) )
            return Response.ok().build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @Path("{home_id}")
    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public Response addLocalStatistic(@PathParam("home_id") String home_id, Statistics stat) {

        int home;

        try {
            home = Integer.parseInt(home_id);
        }

        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Singleton_Statistics instance = Singleton_Statistics.getInstance();

        if ( instance.addLocalStat(stat, home) )
            return Response.ok().build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @Path("{home_id}/{n}")
    @GET
    @Produces("application/json")
    public Response getLastNLocalStatistics(@PathParam("home_id") String home_id, @PathParam("n") String n) {

        int home;
        int number;

        try {
            home = Integer.parseInt(home_id);
            number = Integer.parseInt(n);
        }

        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Singleton_Statistics instance = Singleton_Statistics.getInstance();
        ArrayList<Statistics> localStats = instance.getLocalList().get(home);

        ArrayList<Statistics> result = getLastN(localStats, number);

        if (result != null)
            return Response.ok(result).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @Path("{n}")
    @GET
    @Produces("application/json")
    public Response getLastNGlobalStatistics(@PathParam("n") String n) {

        int number;

        try {
            number = Integer.parseInt(n);
        }

        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Singleton_Statistics instance = Singleton_Statistics.getInstance();
        ArrayList<Statistics> allGlobalStats = instance.getGlobalList();

        ArrayList<Statistics> result = getLastN(allGlobalStats, number);

        if (result != null)
            return Response.ok(result).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();


    }

    @Path("{home_id}/{n}/dev_mean")
    @GET
    @Produces("application/json")
    public Response getDevAndMeanOfLastLocal(@PathParam("home_id") String home_id, @PathParam("n") String n) {

        DevAndMean result = new DevAndMean(-1.0, -1.0);
        List<Double> dataset = new ArrayList<>();

        int home;
        int number;

        Singleton_Statistics instance = Singleton_Statistics.getInstance();

        try {
            home = Integer.parseInt(home_id);
            number = Integer.parseInt(n);
        }

        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        ArrayList<Statistics> localStats, lastN;

        try {
            localStats = instance.getLocalList().get(home);
            lastN = getLastN(localStats, number);
        }

        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            for (int i = 0; i < lastN.size(); i++)
                dataset.add(lastN.get(i).getValue());
        }

        catch (NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


        if ( !dataset.isEmpty() ) {
            result.setMean(
                    instance.mean(dataset)
            );

            result.setStdDev(
                    instance.stdDev(dataset)
            );

            if (result.getMean() == 0.0 && result.getStdDev() == 0)
                return Response.status(Response.Status.BAD_REQUEST).build();
            else
                return Response.ok(result).build();
        }

        else
            return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @Path("{n}/dev_mean")
    @GET
    @Produces("application/json")
    public Response getDevAndMeanOfLastGlobal(@PathParam("n") String n) {

        int number;

        try {
            number = Integer.parseInt(n);
        }

        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        DevAndMean result = new DevAndMean(-1.0, -1.0);
        List<Double> dataset = new ArrayList<>();

        ArrayList<Statistics> globalStats, lastN;

        try {
            Singleton_Statistics instance = Singleton_Statistics.getInstance();
            globalStats = instance.getGlobalList();
            lastN = getLastN(globalStats, number);
        }

        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            for (int i = 0; i < lastN.size(); i++)
                dataset.add(lastN.get(i).getValue());
        }

        catch (NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if ( !dataset.isEmpty() ) {
            result.setMean(
                    Singleton_Statistics.getInstance().mean(dataset)
            );

            result.setStdDev(
                    Singleton_Statistics.getInstance().stdDev(dataset)
            );

            if (result.getMean() == 0.0 && result.getStdDev() == 0) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            else
                return Response.ok(result).build();
        }

        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


    }

    @Path("{home_id}")
    @DELETE
    @Produces("text/plain")
    public Response deleteLocalStats(@PathParam("home_id") String home_id) {

        int home;

        try {
            home = Integer.parseInt(home_id);
        }

        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Singleton_Statistics instance = Singleton_Statistics.getInstance();
        instance.removeLocalStats(home);

        if ( instance.removeLocalStats(home) )
            return Response.ok().build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();

    }

    public ArrayList<Statistics> getLastN(ArrayList<Statistics> dataset, int n) {

        int datasetSize;

        try {
            datasetSize = dataset.size();
        }

        catch (Exception e) {
            return null;
        }

        if ( n > datasetSize ) // SE L'UTENTE CHIEDE UN NUMERO PIU' GRANDE DEL DATASET PRESENTE ESSO VIENE RESTITUITO TUTTO
            return dataset;

        else {
            ArrayList<Statistics> lastN = new ArrayList<>();

            for (int i = datasetSize - 1; i >= datasetSize - n; i--) {
                lastN.add(dataset.get(i));
            }

            return lastN;
        }

    }

}
