define([
    'react',
], function(React) {
    'use strict';

    const ProductList = React.createClass({
        onDelete(itemId, event) {
            event.stopPropagation();
            this.props.onDeleteProduct(itemId);
        },
        render() {
            const { items, loading, error, selected } = this.props.product;

            var itemElements = items.map(item => {
                    var isSelected = selected === item.id;
                    var style = {
                        padding: '0.5em',
                        background: isSelected ? '#0088cc' : 'inherit',
                        color: isSelected ? 'white' : 'inherit'
                    }
                    return (
                        <div onClick={this.props.onSelectProduct.bind(null, item.id)} key={item.id}>
                            <button style={{float: 'right', marginTop: '0.3em'}} onClick={this.onDelete.bind(null, item.id)}>DELETE</button>
                            <p style={style}>{item.title}<br/><i>{item.id}</i></p>
                        </div>
                    )
                }),
                content = loading ? (<p>Loading...</p>) :
                    itemElements.length ? itemElements :
                    error ? (<p>Error</p>) :
                    (<p>No Work Products</p>);

            return (
                <div>
                    <h1>Products</h1>
                    <button onClick={this.props.onCreateGraph}>Create Graph</button>
                    {content}
                </div>
            );
        }
    });

    return ProductList;
});
