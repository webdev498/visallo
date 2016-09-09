define([
    'react',
], function(React) {
    'use strict';

    const ProductsPane = React.createClass({
        render() {
            const { items, loading, error } = this.props.product;

            var itemElements = items.map(function(item) {
                return (<div>
                    <p key={item.id}>{item.title} {item.kind}</p>
                    <pre>
                        {JSON.stringify(item.data, null, 2)}
                    </pre>
                </div>)
            })
            var content =
                loading ? (<p>Loading...</p>) :
                itemElements.length ? itemElements :
                error ? (<p>Error</p>) :
                (<p>No Work Products</p>)

            return (
                <div>
                    <h1>Products</h1>
                    {content}
                </div>
            );
        }
    });

    return ProductsPane;
});
