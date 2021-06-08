package com.ionshield.planner.database;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.LruCache;

import androidx.annotation.NonNull;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.ionshield.planner.PlannerApplication;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PathRepository {
    private static PathRepository INSTANCE;

    private final LruCache<FromTo, DirectionsResult> cache;

    public static PathRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PathRepository();
        }
        return INSTANCE;
    }

    private PathRepository() {
        cache = new LruCache<FromTo, DirectionsResult>(200) {
            @Override
            protected DirectionsResult create(FromTo key) {
                GeoApiContext geoApiContext = getGeoContext();
                try {
                    return DirectionsApi.newRequest(geoApiContext)
                            .origin(key.from)
                            .destination(key.to)
                            .await();
                } catch (ApiException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public DirectionsResult getPath(LatLng from, LatLng to) {
        FromTo fromTo = new FromTo(from, to);
        synchronized (cache) {
            return cache.get(fromTo);
        }
            /*if (cache.containsKey(fromTo)) {
                System.out.println("hit");
                return cache.get(fromTo);
            }
        }
        GeoApiContext geoApiContext = getGeoContext();
        try {
            DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                    .origin(from)
                    .destination(to)
                    .await();
            synchronized (cache) {
                cache.put(fromTo, result);
                System.out.println(cache.size());
            }
            return result;
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    @NonNull
    private GeoApiContext getGeoContext() {
        Context context = PlannerApplication.getApplication().getApplicationContext();
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String key = info.metaData.getString("com.google.android.directions.API_KEY");
            GeoApiContext geoApiContext = new GeoApiContext();
            if (key == null) throw new IllegalArgumentException("No Api Key");
            return geoApiContext.setQueryRateLimit(10)
                    .setApiKey(key)
                    .setConnectTimeout(10, TimeUnit.SECONDS)
                    .setReadTimeout(10, TimeUnit.SECONDS)
                    .setWriteTimeout(10, TimeUnit.SECONDS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Name not found");
    }

    public static class FromTo {
        public LatLng from;
        public LatLng to;

        public FromTo() {
        }

        public FromTo(LatLng from, LatLng to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FromTo fromTo = (FromTo) o;
            return from.lat == fromTo.from.lat &&
                    from.lng == fromTo.from.lng &&
                    to.lat == fromTo.to.lat &&
                    to.lng == fromTo.to.lng;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from.lat, from.lng, to.lat, to.lng);
        }
    }
}
