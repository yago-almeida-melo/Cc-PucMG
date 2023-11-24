async function getObjects() {

    //Topwear
    let topwear = await fetch("https://diwserver.vps.webdock.cloud/products/category/Apparel - Topwear");
    let json1 = await topwear.json();
    objTopwear = json1;

    //Lounge and Night
    let loungeAndNight = await fetch("https://diwserver.vps.webdock.cloud/products/category/Apparel - Loungewear and Nightwear");
    let json2 = await loungeAndNight.json();
    objLoungeAndNight = json2;

    //Innerwear
    let innerW = await fetch("https://diwserver.vps.webdock.cloud/products/category/Apparel - Innerwear");
    let json3 = await innerW.json();
    objInnerW = json3;

    console.log(objTopwear);
    console.log(objLoungeAndNight);
    console.log(objInnerW);

    procurarProdutos();

    let searchValue = getURLParameter('search');
    verifProdutosPesquisa(searchValue);
}

function procurarProdutos() {
    const produtoURL = new URLSearchParams(window.location.search);
    const idParams = produtoURL.get('id');
    console.log(idParams);

    let categoryParams = produtoURL.get('category');
    categoryParams = parseInt(categoryParams);
    console.log(categoryParams);
    console.log(typeof categoryParams);

    var indexProduct;

    switch (categoryParams) {
        case 1:
            indexProduct = objTopwear.products.findIndex(obj => obj.id == idParams);
            exibeProdutos(objTopwear, indexProduct);
            console.log(indexProduct);
            break;
        case 2:
            indexProduct = objLoungeAndNight.products.findIndex(obj => obj.id == idParams);
            exibeProdutos(objLoungeAndNight, indexProduct);
            console.log(indexProduct);
            break;
        case 3:
            indexProduct = objInnerW.products.findIndex(obj => obj.id == idParams);
            exibeProdutos(objInnerW, indexProduct);
            console.log(indexProduct);
            break;
    }
}

function exibeProdutos(obj, index) {
    let produto = document.getElementById('rowProdutos');
    produto.innerHTML += `<div id="title-cat" class="d-flex justify-content-between m-3">
    <h1 id="titulo-produto" class="p-2">${obj.products[index].title}</h1>
    <h1 id="categoria-produto" class="p-2">${obj.products[index].category}</h1>
  </div>
  <div id="produto" class="row align-items-center p-auto">
    <img id="img-produto" class="col-lg-6 col-md-12 col-sm-12 col-xl-6 p-0 mx-auto" src="${obj.products[index].image}" alt="">
    <div id="content" class="col-lg-6 col-md-12 col-sm-12 col-xl-6 text-center mx-auto p-2 align-items-center m-3">
      <div class=" justify-content-center align-items-center">
        <p id="descricao-produto" class="col text-bold text-center mx-auto p-0 fs-3">${obj.products[index].description}</p>
        <p id="preco-produto" class="fs-1 fw-bold">$ ${obj.products[index].price},00</p>
        <p id="avaliacao-produto" class="fs-2">${obj.products[index].rating.rate}★</p>
      </div>
    </div>
  </div>`;
}
window.onload = getObjects;