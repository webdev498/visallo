require(['configuration/plugins/registry'], function(registry) {
    registry.registerExtension('org.visallo.workproduct', {
        identifier: 'org.visallo.web.product.map.MapWorkProduct',
        componentPath: 'org/visallo/web/product/map/MapContainer'
    })
});
