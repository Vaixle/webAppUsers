function checkUncheckAll(oInput) {

        let aInputs = document.getElementsByTagName('input');
        for (var i=0;i<aInputs.length;i++) {
            if (aInputs[i] != oInput) {
                aInputs[i].checked = oInput.checked;
            }
        }


}