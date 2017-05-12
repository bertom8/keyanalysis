window.org_keyanalysis_Services_Complex = function() {
	var connectorId = this.getConnectorId();
	var element = this.getElement();
	
	this.registerRpc({
		sendComplexTypes: function(list, chartId) {
			var message = 'list[2]  = "' + list[2] + '"<br />';
			
			element.innerHTML = message;
		}
	});
}