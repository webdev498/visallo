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
            const { products, loading, error, selected } = this.props;

            var itemElements = products.map(item => {
                    const isSelected = selected === item.id;
                    const style = {
                        padding: '0.5em',
                        background: isSelected ? '#0088cc' : 'inherit',
                        color: isSelected ? 'white' : 'inherit'
                    }
                    const preview = item.previewMD5 ? (
                        <img src={`/product/preview?productId=${encodeURIComponent(item.id)}&workspaceId=${encodeURIComponent(visalloData.currentWorkspaceId)}&md5=${item.previewMD5}`} width="300px" />
                    ) : ''

                    return (
                        <div onClick={this.props.onSelectProduct.bind(null, item.id)} key={item.id}>
                            <button style={{float: 'right', marginTop: '0.3em'}} onClick={this.onDelete.bind(null, item.id)}>DELETE</button>
                            <p style={style}>{item.title}<br/>{item.kind}<br/><i>{item.id}</i><br/>
                            {preview}
                            </p>
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
                    {this.props.types.map(type => {
                        return (
                            <button onClick={this.props.onCreate.bind(this, type)} key={type}>{i18n(type + '.name')}</button>
                        )
                    })}
                    {content}
                </div>
            );
        }
    });

    return ProductList;
});
