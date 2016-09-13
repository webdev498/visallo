define([
    'react',
], function(React) {
    'use strict';

    const ProductPane = React.createClass({
        render() {
            const { items, loading, error, selected } = this.props.product;

            var itemElements = items.map(item => (
                <div onClick={this.props.onSelectProduct.bind(null, item.id)} key={item.id}>
                    <p>{item.title} {item.kind}</p>
                    {selected === item.id ? 'selected' : ''}
                    <pre>
                        {JSON.stringify(item.data, null, 2)}
                    </pre>
                </div>
            ))
            var content =
                loading ? (<p>Loading...</p>) :
                itemElements.length ? itemElements :
                error ? (<p>Error</p>) :
                (<p>No Work Products</p>)

            return (
                <div>
                    <h1>Products</h1>
                    <button onClick={this.props.onCreateGraph}>Create Graph</button>
                    {content}
                </div>
            );
        }
    });

    return ProductPane;
});
