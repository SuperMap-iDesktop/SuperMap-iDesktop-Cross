package com.supermap.desktop.icloud.commontypes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by xie on 2016/12/24.
 */
public class Module {
    private static final Map<String, Module> ALL_BY_CODE = new HashMap<>();
    private final String code;
    private final String name;
    private boolean baseModule;

    public static final class IDesktopModules {
        private static final ProductId[] ALL_PRODUCT_IDS = {ProductId.IDESKTOP_8C_STANDARD, ProductId.IDESKTOP_8C_PROFESSIONAL, ProductId.IDESKTOP_8C_ADVANCED};
        public static final Module STANDARD = Module.newModule("503", "Standard", true, new ProductId[]{ProductId.IDESKTOP_8C_STANDARD});
        public static final Module PROFESSIONAL = Module.newModule("502", "Professional", true, new ProductId[]{ProductId.IDESKTOP_8C_PROFESSIONAL});
        public static final Module ADVANCED = Module.newModule("501", "Advanced", true, new ProductId[]{ProductId.IDESKTOP_8C_ADVANCED});
        public static final Module SPATIAL_ANALYST = Module.newModule("507", "SpatialAnalyst", false, ALL_PRODUCT_IDS);
        public static final Module Network_ANALYST = Module.newModule("505", "NetworkAnalyst", false, ALL_PRODUCT_IDS);
        public static final Module TOPOLOGY = Module.newModule("504", "Topology", false, ALL_PRODUCT_IDS);
        public static final Module CHART = Module.newModule("508", "Chart", false, ALL_PRODUCT_IDS);
        public static final Module EFFECT_3D = Module.newModule("512", "3DEffect", false, ALL_PRODUCT_IDS);
        public static final Module ANALYSt_3D = Module.newModule("510", "3DAnalyst", false, ALL_PRODUCT_IDS);
    }

    public static final class IPortalModules {
        public static final Module IPORTAL = new Module("1009,1010", "IPortal", true);

        static {
            Module.ALL_BY_CODE.put("1009", IPORTAL);
            Module.ALL_BY_CODE.put("1010", IPORTAL);
            Module.ALL_BY_CODE.put("1009,1010", IPORTAL);
        }
    }

    public static final class IServerModules {
        public static final Module STANDARD = Module.newModule("1000", "Standard", true, new ProductId[]{ProductId.ISERVER_8C_STANDARD});
        public static final Module PROFESSIONAL = Module.newModule("1001", "Professional", true, new ProductId[]{ProductId.ISERVER_8C_PROFESSIONAL});
        public static final Module ADVANCED = Module.newModule("1002", "Advanced", true, new ProductId[]{ProductId.ISERVER_8C_ADVANCED});
        private static final ProductId[] ALL_PRODUCT_IDS = {ProductId.ISERVER_8C_STANDARD, ProductId.ISERVER_8C_PROFESSIONAL, ProductId.ISERVER_8C_ADVANCED};
        public static final Module SPATIAL_ANALYST = Module.newModule("1003", "SpatialAnalyst", false, ALL_PRODUCT_IDS);
        public static final Module Network_ANALYST = Module.newModule("1004", "NetworkAnalyst", false, ALL_PRODUCT_IDS);
        public static final Module TRAFFIC_TRANSFER_ANALYST = Module.newModule("1005", "TrafficTransferAnalyst", false, ALL_PRODUCT_IDS);
        public static final Module REALSPACE = Module.newModule("1006", "Realspace", false, ALL_PRODUCT_IDS);
        public static final Module CHART = Module.newModule("1007", "Chart", false, ALL_PRODUCT_IDS);
        public static final Module SPATIAL_ANALYST_3D = Module.newModule("1012", "3DSpatialAnalyst", false, ALL_PRODUCT_IDS);
        public static final Module Network_ANALYST_3D = Module.newModule("1011", "3DNetworkAnalyst", false, ALL_PRODUCT_IDS);
    }

    public static final class IExpressModules {
    }

    public static final class IObjectcModules {
        private static final ProductId[] ALL_PRODUCT_IDS = new ProductId[0];
        public static final Module spatial_database = Module.newModule("3", "SpatialDatabase", false, ALL_PRODUCT_IDS);
        public static final Module spatial_analyst = Module.newModule("9", "SpatialAnalyst", false, ALL_PRODUCT_IDS);
        public static final Module network_analyst = Module.newModule("11", "NetworkAnalyst", false, ALL_PRODUCT_IDS);
        public static final Module TOPOLOGY = Module.newModule("13", "Topology", false, ALL_PRODUCT_IDS);
        public static final Module Address_Match = Module.newModule("15", "AddressMatch", false, ALL_PRODUCT_IDS);
        public static final Module TRAFFIC_TRANSFER_ANALYST = Module.newModule("27", "TrafficTransferAnalyst", false, ALL_PRODUCT_IDS);
        public static final Module scene_3d = Module.newModule("5", "3DScene", false, ALL_PRODUCT_IDS);
        public static final Module CHART = Module.newModule("22", "Chart", false, ALL_PRODUCT_IDS);
        public static final Module SPATIAL_ANALYST_3D = Module.newModule("24", "3DSpatialAnalyst", false, ALL_PRODUCT_IDS);
        public static final Module Network_ANALYST_3D = Module.newModule("29", "3DNetworkAnalyst", false, ALL_PRODUCT_IDS);
        public static final Module EFFECT_3D = Module.newModule("31", "3DEffect", false, ALL_PRODUCT_IDS);
    }

    public static final class IMobileModules {
        private static final ProductId[] DEV_PRODUCT_IDS = new ProductId[0];
        public static final Module NAVIGATION_DEV = Module.newModule("4", "Navigation", false, DEV_PRODUCT_IDS);
        public static final Module SCENE_3D_DEV = Module.newModule("16", "3DSceneDevlop", false, DEV_PRODUCT_IDS);
    }

    public static final class ICloudManagerModules {
        public static final Module NAVIGATION_DEV = Module.newModule("1013", "BASE", true, new ProductId[0]);
    }

    static {
        new IDesktopModules();
        new ICloudManagerModules();
        new IMobileModules();
        new IServerModules();
        new IObjectcModules();
    }

    private static Module newModule(String code, String name, boolean isBase, ProductId... productIds) {
        Module result = new Module(code, name, isBase);
        ALL_BY_CODE.put(result.code, result);
        return result;
    }

    public static Module valueOf(String code) {
        return ALL_BY_CODE.containsKey(code) ? ALL_BY_CODE.get(code) : new Module(code, "UNKNOWN");
    }

    public Module(String paramCode, String paramName) {
        this(paramCode, paramName, false);
    }

    public Module(String paramCode, String paramName, boolean paramIsBaseModule) {
        this.baseModule = paramIsBaseModule;
        this.code = paramCode;
        this.name = paramName;
    }

    public boolean isBaseModule() {
        return this.baseModule;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getLocaleName(Locale localse) {
        return getName();
    }

    public String getLocaleName() {
        return getLocaleName(Locale.getDefault());
    }

    public String toString() {
        return getName();
    }
}
