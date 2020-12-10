(this["webpackJsonpbudget-tracker"]=this["webpackJsonpbudget-tracker"]||[]).push([[0],{113:function(e,t,a){},176:function(e,t,a){"use strict";a.r(t);var n=a(1),s=a(0),c=a(35),i=a.n(c),r=a(25),o=(a(93),a(5)),l=a(6),d=a(8),h=a(7),m=a(12),j=a(9),b=(a(64),a.p+"static/media/logo.f9879d35.png"),u=a(20),O=a.n(u),p=a(81),g=Object({NODE_ENV:"production",PUBLIC_URL:"/Budget-Tracker",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0,FAST_REFRESH:!0,REACT_APP_API_URL:"http://34.71.254.156:8080"}).REACT_APP_API_RUL+"/api/auth/",x=new(function(){function e(){Object(o.a)(this,e)}return Object(l.a)(e,[{key:"login",value:function(e,t){return O.a.post(g+"signin",{username:e,password:t}).then((function(e){if(e.data.access_token)return localStorage.setItem("user",JSON.stringify(e.data)),e.data;console.log(e)}))}},{key:"signup",value:function(e,t,a){return O.a.post(g+"signup",{name:e,username:t,password:a}).then((function(e){if(e.data.access_token)return localStorage.setItem("user",JSON.stringify(e.data)),e.data;console.log(e)}))}},{key:"signout",value:function(){localStorage.removeItem("user")}},{key:"isLoggedIn",value:function(){var e=JSON.parse(localStorage.getItem("user"));if(e&&e.access_token){var t=e.access_token,a=Object(p.a)(t),n=Date.now()/1e3;if(a.exp>n)return!0}return!1}},{key:"getName",value:function(){var e=JSON.parse(localStorage.getItem("user"));return e&&e.name?e.name:"null"}}]),e}()),f=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){var n;return Object(o.a)(this,a),(n=t.call(this,e)).state={username:"",password:"",message:"",loading:!1},n.handleChange=n.handleChange.bind(Object(j.a)(n)),n.handleSubmit=n.handleSubmit.bind(Object(j.a)(n)),n}return Object(l.a)(a,[{key:"handleChange",value:function(e){var t=e.target,a=t.value,n=t.name,s=Object.assign({},this.state);s[n]=a,this.setState(s)}},{key:"handleSubmit",value:function(e){var t=this;e.preventDefault();var a=Object.assign({},this.state);a.message="",a.loading=!0,this.setState(a),x.login(this.state.username,this.state.password).then((function(e){console.log(e),t.props.history.push("/dashboard"),window.location.reload()})).catch((function(e){e.response?t.setState({message:e.response.data}):t.setState({message:"server connection failed"})})).finally((function(){t.setState({loading:!1})}))}},{key:"render",value:function(){return Object(n.jsxs)("div",{className:"text-center center",children:[Object(n.jsx)("img",{className:"mb-4",src:b,alt:"",width:"72",height:"72"}),Object(n.jsx)("h1",{className:"h3 mb-3 font-weight-normal",children:"Please Sign In"}),Object(n.jsxs)("form",{className:"form-signin",onSubmit:this.handleSubmit,children:[Object(n.jsx)("input",{type:"username",name:"username",className:"form-control signin",placeholder:"Username",onChange:this.handleChange,required:!0,autoFocus:!0}),Object(n.jsx)("input",{type:"password",name:"password",className:"form-control signin",placeholder:"Password",required:!0,onChange:this.handleChange}),this.state.message&&Object(n.jsx)("div",{className:"alert alert-danger",role:"alert",children:Object(n.jsx)("p",{children:this.state.message})}),Object(n.jsx)("a",{href:"/signup",children:"Create new account"}),Object(n.jsx)("button",{type:"submit",className:"btn btn-primary btn-block btn-smbt",disabled:this.state.loading,children:"Login"})]})]})}}]),a}(s.Component),v=a(82),N=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){return Object(o.a)(this,a),t.call(this,e)}return Object(l.a)(a,[{key:"render",value:function(){var e=this.props.amounts;return Object(n.jsxs)("div",{className:"Container",children:[Object(n.jsx)("h3",{children:"This Week's Expense"}),Object(n.jsx)(v.a,{width:"500px",height:"300px",chartType:"Bar",loader:Object(n.jsx)("div",{children:"Loading Chart"}),data:[["",""],["MON",e[0]],["TUE",e[1]],["WED",e[2]],["THU",e[3]],["FRI",e[4]],["SAT",e[5]],["SUN",e[6]]],options:{legend:{position:"none"}}})]})}}]),a}(s.Component);function y(){var e=JSON.parse(localStorage.getItem("user"));return e&&e.access_token?{Authorization:"Bearer "+e.access_token}:{}}var k=Object({NODE_ENV:"production",PUBLIC_URL:"/Budget-Tracker",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0,FAST_REFRESH:!0,REACT_APP_API_URL:"http://34.71.254.156:8080"}).REACT_APP_API_RUL+"/api/user/",S=new(function(){function e(){Object(o.a)(this,e)}return Object(l.a)(e,[{key:"postNewTransaction",value:function(e){return O.a.post(k+"new-transaction",e,{headers:y()})}},{key:"getTransaction",value:function(e){return O.a.get(k+"transaction",{params:{id:e},headers:y()})}},{key:"getSearchTransaction",value:function(e){return O.a.get(k+"search-transaction",{params:{key:e},headers:y()})}},{key:"postDeleteTransaction",value:function(e){return O.a.post(k+"delete-transaction",{id:e},{headers:y()})}},{key:"getWeeklySummary",value:function(){return O.a.get(k+"week-summary",{headers:y()})}},{key:"getBalance",value:function(){return O.a.get(k+"balance",{headers:y()})}},{key:"getRecentTransaction",value:function(){return O.a.get(k+"recent-transaction",{headers:y()})}}]),e}()),C=(a(113),function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){return Object(o.a)(this,a),t.call(this,e)}return Object(l.a)(a,[{key:"render",value:function(){return Object(n.jsx)("div",{children:Object(n.jsx)("div",{className:"card tran bg-light mb-3",children:Object(n.jsxs)("div",{className:"card-body lil-pad",children:[Object(n.jsx)("a",{href:"transaction/"+this.props.id,children:Object(n.jsxs)("h5",{className:"mb-0",children:[this.props.merchantName,Object(n.jsx)("span",{className:"badge badge-secondary ml-2",children:this.props.type})]})}),Object(n.jsxs)("div",{className:"row zero-line-height",children:[Object(n.jsxs)("p",{className:"card-text col-8 mb-0",children:[this.props.location,Object(n.jsx)("br",{}),this.props.date]}),Object(n.jsx)("p",{className:"card-text col-4 float-right h5",children:this.props.amount})]})]})})})}}]),a}(s.Component)),w=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){return Object(o.a)(this,a),t.call(this,e)}return Object(l.a)(a,[{key:"render",value:function(){var e={cash:"Cash",credit:"Credit/Debit",payment:"Payment",transfer:"Transfer"};return Object(n.jsx)("div",{children:this.props.transactions.map((function(t){return Object(n.jsx)(C,{id:t.id,merchantName:t.merchantName,type:e[t.type],location:t.location,date:new Date(t.date).toDateString(),amount:("income"===t.status?"+":"-")+"$"+t.amount},t.id)}))})}}]),a}(s.Component),T=(a(50),function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){var n;return Object(o.a)(this,a),(n=t.call(this,e)).state={amounts:[0,0,0,0,0,0,0],balance:0,recentTransactions:[]},n}return Object(l.a)(a,[{key:"componentDidMount",value:function(){var e=this;S.getWeeklySummary().then((function(t){console.log(t),e.setState({amounts:t.data.amounts})})).catch((function(e){console.log(e)})),S.getBalance().then((function(t){console.log(t),e.setState({balance:t.data.balance})})).catch((function(e){console.log(e)})),S.getRecentTransaction().then((function(t){console.log(t),e.setState({recentTransactions:t.data.transactions})})).catch((function(e){console.log(e)}))}},{key:"render",value:function(){return Object(n.jsxs)("div",{className:"containter",children:[Object(n.jsx)("h2",{className:"mt-4 mb-4 text-center ",children:"Dashboard"}),Object(n.jsxs)("div",{className:"row content-center",children:[Object(n.jsx)("div",{className:"ml-5 mb-0 col-sm-12 col-md-8 col-lg-6",children:Object(n.jsx)("div",{className:"content-center",children:Object(n.jsx)(N,{amounts:this.state.amounts})})}),Object(n.jsx)("div",{className:"ml-5 mr-5 col-md-6 col-lg-4",children:Object(n.jsx)("div",{className:"content-center pt-5",children:Object(n.jsxs)("h1",{children:["Balance: $",this.state.balance]})})})]}),Object(n.jsxs)("div",{className:"container mt-4",children:[Object(n.jsx)("h3",{children:"Recent"}),Object(n.jsx)(w,{transactions:this.state.recentTransactions})]})]})}}]),a}(s.Component)),D=a(38),R=a.n(D),M=(a(51),function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){var n;return Object(o.a)(this,a),(n=t.call(this,e)).state={minDate:new Date,maxDate:new Date,minAmount:"20.00",maxAmount:"35.00",type:"3"},n.handleMinDateChange=n.handleMinDateChange.bind(Object(j.a)(n)),n.handleMaxDateChange=n.handleMaxDateChange.bind(Object(j.a)(n)),n}return Object(l.a)(a,[{key:"componentDidUpdate",value:function(){console.log(this.state.minDate.toISOString())}},{key:"handleMinDateChange",value:function(e){this.setState({minDate:e}),console.log(e)}},{key:"handleMaxDateChange",value:function(e){this.setState({maxDate:e}),console.log(e)}},{key:"render",value:function(){return Object(n.jsxs)("div",{children:[Object(n.jsxs)("div",{className:"input-group mb-3",children:[Object(n.jsx)(R.a,{className:"form-control",name:"minDate",placeholderText:"Start date",selected:this.state.minDate,onChange:this.handleMinDateChange}),Object(n.jsx)("span",{className:"input-group-text",children:"\u2194"}),Object(n.jsx)(R.a,{className:"form-control",name:"maxDate",placeholderText:"EndDate",selected:this.state.maxDate,onChange:this.handleMaxDateChange})]}),Object(n.jsx)("form",{children:Object(n.jsxs)("div",{class:"form-group",children:[Object(n.jsx)("label",{for:"formControlRange",children:"Example Range input"}),Object(n.jsx)("input",{type:"range",class:"form-control-range",id:"formControlRange"})]})})]})}}]),a}(s.Component)),_=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){var n;return Object(o.a)(this,a),(n=t.call(this,e)).state={name:"",username:"",password:"",message:"",loading:!1},n.handleChange=n.handleChange.bind(Object(j.a)(n)),n.handleSubmit=n.handleSubmit.bind(Object(j.a)(n)),n}return Object(l.a)(a,[{key:"handleChange",value:function(e){var t=e.target,a=t.value,n=t.name,s=Object.assign({},this.state);s[n]=a,this.setState(s)}},{key:"handleSubmit",value:function(e){var t=this;e.preventDefault();var a=Object.assign({},this.state);a.message="",a.loading=!0,this.setState(a),x.signup(this.state.name,this.state.username,this.state.password).then((function(e){console.log(e),t.props.history.push("/dashboard"),window.location.reload()})).catch((function(e){e.response?t.setState({message:e.response.data}):t.setState({message:"server connection failed"})})).finally((function(){t.setState({loading:!1})}))}},{key:"render",value:function(){return Object(n.jsxs)("div",{className:"text-center center",children:[Object(n.jsx)("img",{className:"mb-4",src:b,alt:"",width:"72",height:"72"}),Object(n.jsx)("h1",{className:"h3 mb-3 font-weight-normal",children:"Please Sign up"}),Object(n.jsxs)("form",{className:"form-signin",onSubmit:this.handleSubmit,children:[Object(n.jsx)("input",{type:"text",name:"name",className:"form-control signin",placeholder:"Name",onChange:this.handleChange,required:!0,autoFocus:!0}),Object(n.jsx)("br",{}),Object(n.jsx)("input",{type:"username",name:"username",className:"form-control signin",placeholder:"Username",onChange:this.handleChange,required:!0}),Object(n.jsx)("input",{type:"password",name:"password",className:"form-control signin",placeholder:"Password",required:!0,onChange:this.handleChange}),this.state.message&&Object(n.jsx)("div",{className:"alert alert-danger",role:"alert",children:Object(n.jsx)("p",{children:this.state.message})}),Object(n.jsx)("a",{href:"/signin",children:"Already have an account?"}),Object(n.jsx)("button",{type:"submit",className:"btn btn-primary btn-block btn-smbt",disabled:this.state.loading,children:"Sign up"})]})]})}}]),a}(s.Component),A=a(39),I=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){var n;return Object(o.a)(this,a),(n=t.call(this,e)).state={name:"My Account"},n.handleSignout=n.handleSignout.bind(Object(j.a)(n)),n}return Object(l.a)(a,[{key:"componentDidMount",value:function(){this.setState({name:x.getName()}),console.log(x.isLoggedIn())}},{key:"handleSignout",value:function(){console.log("hello"),x.signout(),window.location.href="/Budget-Tracker/"}},{key:"render",value:function(){return Object(n.jsxs)("nav",{className:"navbar navbar-expand-md navbar-light bg-light fixed-top",children:[Object(n.jsxs)("a",{className:"navbar-brand",href:"/Budget-Tracker/",children:[Object(n.jsx)("img",{src:b,width:"30",height:"30",className:"mr-1 d-inline-block align-top",alt:""}),"Budget Tracker"]}),Object(n.jsx)("button",{className:"navbar-toggler",type:"button","data-toggle":"collapse","data-target":"#navbarNav","aria-controls":"navbarNav","aria-expanded":"false","aria-label":"Toggle navigation",children:Object(n.jsx)("span",{className:"navbar-toggler-icon"})}),Object(n.jsxs)("div",{className:"collapse navbar-collapse justify-content-end",id:"navbarNav",children:[Object(n.jsxs)("ul",{className:"navbar-nav",children:[x.isLoggedIn()&&Object(n.jsx)("li",{className:"nav-item",children:Object(n.jsx)("a",{className:"nav-link",href:"/Budget-Tracker/dashboard",children:"Dashboard"})}),x.isLoggedIn()&&Object(n.jsx)("li",{className:"nav-item",children:Object(n.jsx)("a",{className:"nav-link",href:"/Budget-Tracker/search",children:"Search"})}),Object(n.jsx)("li",{className:"nav-item",children:Object(n.jsx)("a",{className:"nav-link",href:"/Budget-Tracker/about",children:"About"})})]}),Object(n.jsxs)("ul",{className:"navbar-nav ml-auto mr-1",children:[x.isLoggedIn()&&Object(n.jsx)("li",{className:"nav-item",children:Object(n.jsxs)("a",{className:"nav-link",href:"/Budget-Tracker/new",children:[Object(n.jsx)(A.b,{className:"mr-2",size:16}),"Add Transaction"]})}),x.isLoggedIn()?Object(n.jsxs)("li",{className:"nav-item dropdown",children:[Object(n.jsxs)("span",{className:"nav-link dropdown-toggle","data-toggle":"dropdown","aria-haspopup":"true","aria-expanded":"false",children:[Object(n.jsx)(A.a,{className:"mr-2",size:16}),this.state.name]}),Object(n.jsx)("div",{className:"dropdown-menu dropdown-menu-right","aria-labelledby":"navbarDropdownMenuLink",children:Object(n.jsxs)("span",{className:"dropdown-item",onClick:this.handleSignout,children:[Object(n.jsx)(A.d,{className:"mr-2",size:16}),"Sign Out"]})})]}):Object(n.jsx)("li",{className:"nav-item",children:Object(n.jsxs)("a",{className:"nav-link",href:"/Budget-Tracker/signin",children:[Object(n.jsx)(A.c,{className:"mr-2",size:16}),"Sign In"]})})]})]})]})}}]),a}(s.Component),B=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(){return Object(o.a)(this,a),t.apply(this,arguments)}return Object(l.a)(a,[{key:"render",value:function(){return Object(n.jsx)("div",{className:"modal fade",id:"redirectModal",tabIndex:"-1",role:"dialog",children:Object(n.jsx)("div",{className:"modal-dialog",role:"document",children:Object(n.jsxs)("div",{className:"modal-content",children:[Object(n.jsx)("div",{className:"modal-header",children:Object(n.jsx)("h5",{className:"modal-title",id:"redirectModalLabel",children:this.props.title})}),Object(n.jsx)("div",{className:"modal-body",children:this.props.message}),Object(n.jsx)("div",{className:"modal-footer",children:Object(n.jsx)("button",{type:"button",className:"btn btn-primary",onClick:this.props.onConfirm,children:"OK"})})]})})})}}]),a}(s.Component),E=(a(79),function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){var n;return Object(o.a)(this,a),(n=t.call(this,e)).state={loading:!1,form:{merchantName:"",location:"",amount:"",status:"expense",date:new Date,type:"",description:""},resTitle:"",resMessage:"",resRedir:""},n.handleChange=n.handleChange.bind(Object(j.a)(n)),n.handleDateChange=n.handleDateChange.bind(Object(j.a)(n)),n.handleSubmit=n.handleSubmit.bind(Object(j.a)(n)),n.handleSuccess=n.handleSuccess.bind(Object(j.a)(n)),n}return Object(l.a)(a,[{key:"handleSuccess",value:function(e){this.props.history.push("/dashboard"),window.location.reload()}},{key:"handleDateChange",value:function(e){var t=Object.assign({},this.state.form);t.date=e,this.setState({form:t})}},{key:"handleChange",value:function(e){console.log(e);var t=e.target,a=t.value,n=t.name,s=Object.assign({},this.state.form);s[n]=a,this.setState({form:s}),console.log(s)}},{key:"handleSubmit",value:function(e){var t=this;e.preventDefault();var a=Object.assign({},this.state.form);a.date=a.date.toISOString(),S.postNewTransaction(a).then((function(e){console.log("response",e),t.setState({resTitle:"Success",resMessage:e.data.msg,resRedir:"/dashboard"}),document.getElementById("launchRedirectModalButton").click()})).catch((function(e){console.log("error",e),t.setState({resTitle:"Fail",resMessage:e.response.data.msg,resRedir:"/new"}),document.getElementById("launchRedirectModalButton").click()}))}},{key:"render",value:function(){return Object(n.jsxs)("div",{className:"container col-lg-8",children:[Object(n.jsx)("h2",{className:"mt-4 mb-4 text-center ",children:"Create New Transaction"}),Object(n.jsxs)("form",{onSubmit:this.handleSubmit,children:[Object(n.jsxs)("div",{className:"form-group row align-items-center",children:[Object(n.jsx)("label",{forhtml:"inputMerchantName",className:"col-sm-2 col-form-label",children:"Merchant name"}),Object(n.jsx)("div",{className:"col-sm-10",children:Object(n.jsx)("input",{type:"text",className:"form-control",name:"merchantName",id:"inputMerchantName",placeholder:"Marriott",onChange:this.handleChange,required:!0,autoFocus:!0})})]}),Object(n.jsxs)("div",{className:"form-group row align-items-center",children:[Object(n.jsx)("label",{forhtml:"inputLocation",className:"col-sm-2 col-form-label",children:"Location"}),Object(n.jsx)("div",{className:"col-sm-10",children:Object(n.jsx)("input",{type:"text",className:"form-control",name:"location",id:"inputLocation",placeholder:"New York, NY",onChange:this.handleChange})})]}),Object(n.jsxs)("div",{className:"form-group row align-items-center",children:[Object(n.jsx)("label",{forhtml:"inputAmount",className:"col-sm-2 col-form-label",children:"Amount"}),Object(n.jsx)("div",{className:"col-sm-10",children:Object(n.jsxs)("div",{className:"input-group",children:[Object(n.jsx)("div",{className:"input-group-prepend",children:Object(n.jsx)("div",{className:"input-group-text",children:"income"===this.state.form.status?"+":"-"})}),Object(n.jsx)("input",{type:"number",className:"form-control",id:"inputAmount",placeholder:"0.00",min:"0",step:"0.01",name:"amount",onChange:this.handleChange,required:!0}),Object(n.jsx)("div",{className:"input-group-append",children:Object(n.jsxs)("select",{name:"status",value:this.state.form.status,onChange:this.handleChange,id:"inputAmountStatus",className:"form-control",children:[Object(n.jsx)("option",{value:"expense",children:"Expense"}),Object(n.jsx)("option",{value:"income",children:"Income"})]})})]})})]}),Object(n.jsxs)("div",{className:"form-group row align-items-center",children:[Object(n.jsx)("label",{forhtml:"inputDate",className:"col-sm-2 col-form-label",children:"Date"}),Object(n.jsx)("div",{className:"col-sm-10",children:Object(n.jsx)(R.a,{className:"form-control",name:"date",placeholderText:"Date",selected:this.state.form.date,onChange:this.handleDateChange})})]}),Object(n.jsxs)("div",{className:"form-group row align-items-center",children:[Object(n.jsx)("label",{forhtml:"inputType",className:"col-sm-2 col-form-label",children:"Type"}),Object(n.jsx)("div",{className:"col-sm-10",children:Object(n.jsxs)("div",{className:"form-control",id:"inputType",children:[Object(n.jsxs)("div",{className:"form-check form-check-inline",onChange:this.handleChange,children:[Object(n.jsx)("input",{className:"form-check-input",type:"radio",name:"type",id:"inlineRadio1",value:"cash",required:!0}),Object(n.jsx)("label",{className:"form-check-label",forhtml:"inlineRadio1",children:"Cash"})]}),Object(n.jsxs)("div",{className:"form-check form-check-inline",onChange:this.handleChange,children:[Object(n.jsx)("input",{className:"form-check-input",type:"radio",name:"type",id:"inlineRadio2",value:"credit"}),Object(n.jsx)("label",{className:"form-check-label",forhtml:"inlineRadio2",children:"Credit/Debit"})]}),Object(n.jsxs)("div",{className:"form-check form-check-inline",onChange:this.handleChange,children:[Object(n.jsx)("input",{className:"form-check-input",type:"radio",name:"type",id:"inlineRadio3",value:"payment"}),Object(n.jsx)("label",{className:"form-check-label",forhtml:"inlineRadio3",children:"Payment"})]}),Object(n.jsxs)("div",{className:"form-check form-check-inline",onChange:this.handleChange,children:[Object(n.jsx)("input",{className:"form-check-input",type:"radio",name:"type",id:"inlineRadio4",value:"transfer"}),Object(n.jsx)("label",{className:"form-check-label",forhtml:"inlineRadio4",children:"Transfer"})]})]})})]}),Object(n.jsxs)("div",{className:"form-group row",children:[Object(n.jsx)("label",{className:"col-sm-2 col-form-label",forhtml:"inputDescription",children:"Description"}),Object(n.jsx)("div",{className:"col-sm-10",children:Object(n.jsx)("textarea",{className:"form-control",name:"description",id:"inputDescription",rows:"3",onChange:this.handleChange})})]}),Object(n.jsx)("button",{type:"submit",className:"btn btn-primary btn-block btn-smbt col-6 mx-auto",disabled:this.state.loading,children:"Add Transaction"})]}),Object(n.jsx)("button",{type:"button",id:"launchRedirectModalButton",className:"btn btn-primary","data-toggle":"modal","data-target":"#redirectModal",hidden:!0,children:"Launch redirect Modal"}),Object(n.jsx)(B,{title:this.state.resTitle,to:this.state.resRedir,message:this.state.resMessage,onConfirm:this.handleSuccess})]})}}]),a}(s.Component)),L=a(47),P=a(87);var U=function(e){var t=e.component,a=Object(P.a)(e,["component"]);return Object(n.jsx)(m.b,Object(L.a)(Object(L.a)({},a),{},{render:function(e){return x.isLoggedIn()?Object(n.jsx)(t,Object(L.a)({},e)):Object(n.jsx)(m.a,{to:"/signin"})}}))},F=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){var n;return Object(o.a)(this,a),(n=t.call(this,e)).state={loaded:!1,loading:!1,id:e.match.params.id,merchantName:"Apple Store",location:"New York, NY",amount:"12.99",status:"expense",date:new Date,type:"credit",description:"some desc some desc some desc some d",resTitle:"",resMessage:"",resRedir:""},n.handleDelete=n.handleDelete.bind(Object(j.a)(n)),n.handleSuccess=n.handleSuccess.bind(Object(j.a)(n)),n}return Object(l.a)(a,[{key:"componentDidMount",value:function(){var e=this;console.log(this.state.id),S.getTransaction(this.state.id).then((function(t){console.log(t);var a=Object.assign({},t.data);a.date=new Date(a.date),console.log(a),a.loaded=!0,e.setState(a)})).catch((function(t){console.log(t),e.props.history.push("/page-not-found"),window.location.reload()}))}},{key:"handleSuccess",value:function(e){this.props.history.push("/dashboard"),window.location.reload()}},{key:"handleDelete",value:function(){var e=this;this.setState({loading:!0}),S.postDeleteTransaction(this.state.id).then((function(t){console.log(t),e.setState({resTitle:"Success",resMessage:t.data.msg,resRedir:"/dashboard"}),document.getElementById("launchRedirectModalButton").click()})).catch((function(t){e.setState({resTitle:"Fail",resMessage:t.response.data.msg,resRedir:window.location.pathname}),document.getElementById("launchRedirectModalButton").click()})).finally((function(){e.setState({loading:!1})}))}},{key:"render",value:function(){var e=("income"===this.state.status?"+":"-")+"$"+this.state.amount;return this.state.loaded?Object(n.jsxs)("div",{className:"container col-lg-8",children:[Object(n.jsx)("h2",{className:"mt-4 mb-4 text-center ",children:e+", "+this.state.merchantName}),Object(n.jsx)("div",{className:"row justify-content-center",children:Object(n.jsxs)("div",{className:"col-auto",children:[Object(n.jsx)("table",{className:"table mb-0",children:Object(n.jsxs)("tbody",{children:[Object(n.jsxs)("tr",{children:[Object(n.jsx)("th",{children:"Merchant name"}),Object(n.jsx)("td",{children:this.state.merchantName})]}),Object(n.jsxs)("tr",{children:[Object(n.jsx)("th",{children:"Location"}),Object(n.jsx)("td",{children:this.state.location})]}),Object(n.jsxs)("tr",{children:[Object(n.jsx)("th",{children:"Amount"}),Object(n.jsx)("td",{children:e})]}),Object(n.jsxs)("tr",{children:[Object(n.jsx)("th",{children:"Date"}),Object(n.jsx)("td",{children:this.state.date.toDateString()})]}),Object(n.jsxs)("tr",{children:[Object(n.jsx)("th",{children:"Type"}),Object(n.jsx)("td",{children:{cash:"Cash",credit:"Credit/Debit",payment:"Payment",transfer:"Transfer"}[this.state.type]})]}),Object(n.jsxs)("tr",{children:[Object(n.jsx)("th",{children:"Description"}),Object(n.jsx)("td",{children:Object(n.jsx)("textarea",{id:"viewTranTextArea",value:this.state.description,disabled:!0})})]})]})}),Object(n.jsx)("button",{type:"submit mt-0 mb-4",className:"btn btn-danger btn-block btn-smbt mx-auto",onClick:this.handleDelete,disabled:this.state.loading,children:"Delete Transaction"})]})}),Object(n.jsx)("button",{type:"button",id:"launchRedirectModalButton",className:"btn btn-primary","data-toggle":"modal","data-target":"#redirectModal",hidden:!0,children:"Launch redirect Modal"}),Object(n.jsx)(B,{title:this.state.resTitle,to:this.state.resRedir,message:this.state.resMessage,onConfirm:this.handleSuccess})]}):null}}]),a}(s.Component),W=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){return Object(o.a)(this,a),t.call(this,e)}return Object(l.a)(a,[{key:"render",value:function(){return Object(n.jsx)("div",{calssName:"container",children:Object(n.jsx)("h2",{className:"mt-4 mb-4 text-center",children:"Page not found"})})}}]),a}(s.Component),K=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){var n;return Object(o.a)(this,a),(n=t.call(this,e)).state={key:"",transactions:[]},n.handleChange=n.handleChange.bind(Object(j.a)(n)),n.handleSubmit=n.handleSubmit.bind(Object(j.a)(n)),n}return Object(l.a)(a,[{key:"handleChange",value:function(e){var t=e.target,a=t.value,n=t.name,s=Object.assign({},this.state);s[n]=a,this.setState(s)}},{key:"handleSubmit",value:function(e){var t=this;console.log(this.state),e.preventDefault(),S.getSearchTransaction(this.state.key).then((function(e){console.log(e),t.setState({transactions:e.data.transactions})})).catch((function(e){console.log(e),t.props.history.push("/page-not-found"),window.location.reload()}))}},{key:"render",value:function(){return Object(n.jsxs)("div",{className:"container",children:[Object(n.jsx)("h2",{className:"mt-4 mb-4 text-center ",children:"Search"}),Object(n.jsxs)("form",{className:"form-inline justify-content-center mb-4",onSubmit:this.handleSubmit,children:[Object(n.jsx)("input",{type:"text",name:"key",className:"form-control col-8 mr-2",id:"inputKeyword",placeholder:"Keywords",onChange:this.handleChange}),Object(n.jsx)("button",{type:"submit",className:"btn btn-primary col-2",children:"Search"})]}),Object(n.jsx)(w,{transactions:this.state.transactions})]})}}]),a}(s.Component),q=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){var n;return Object(o.a)(this,a),(n=t.call(this,e)).handleClick=n.handleClick.bind(Object(j.a)(n)),n}return Object(l.a)(a,[{key:"handleClick",value:function(){this.props.history.push("/signup"),window.location.reload()}},{key:"render",value:function(){return x.isLoggedIn()?Object(n.jsx)(m.a,{to:"/dashboard"}):Object(n.jsxs)("div",{className:"text-center mt-5",children:[Object(n.jsx)("h1",{children:"Welcome to Budget Tracker!"}),Object(n.jsx)("button",{type:"submit mt-4",className:"btn btn-primary btn-block btn-smbt col-2 mx-auto",onClick:this.handleClick,children:"Sign Up"})]})}}]),a}(s.Component),J=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){return Object(o.a)(this,a),t.call(this,e)}return Object(l.a)(a,[{key:"render",value:function(){return Object(n.jsx)("div",{className:"row mb-5 justify-content-center footer",children:Object(n.jsxs)("h6",{children:[" ",Object(n.jsx)("small",{children:"copyright \xa9 2020 Jerry Xu"})]})})}}]),a}(s.Component),H=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(e){return Object(o.a)(this,a),t.call(this,e)}return Object(l.a)(a,[{key:"componentDidMount",value:function(){window.location="https://flipgrid.com/d6f8a516"}},{key:"render",value:function(){return Object(n.jsx)("div",{calssName:"container",children:Object(n.jsx)("h2",{className:"mt-4 mb-4 text-center",children:"Redirecting..."})})}}]),a}(s.Component),z=function(e){Object(d.a)(a,e);var t=Object(h.a)(a);function a(){return Object(o.a)(this,a),t.apply(this,arguments)}return Object(l.a)(a,[{key:"render",value:function(){return Object(n.jsx)("div",{className:"App",children:Object(n.jsxs)(r.a,{basename:"/Budget-Tracker",children:[Object(n.jsx)(I,{}),Object(n.jsxs)(m.d,{children:[Object(n.jsx)(m.b,{exact:!0,path:"/",component:q}),Object(n.jsx)(m.b,{exact:!0,path:"/signin",component:f}),Object(n.jsx)(m.b,{exact:!0,path:"/signup",component:_}),Object(n.jsx)(U,{exact:!0,path:"/dashboard",component:T}),Object(n.jsx)(m.b,{exact:!0,path:"/filter",component:M}),Object(n.jsx)(U,{exact:!0,path:"/new",component:E}),Object(n.jsx)(U,{exact:!0,path:"/transaction/:id",component:F}),Object(n.jsx)(m.b,{exact:!0,path:"/page-not-found",component:W}),Object(n.jsx)(m.b,{exact:!0,path:"/cell",component:w}),Object(n.jsx)(m.b,{exact:!0,path:"/search",component:K}),Object(n.jsx)(m.b,{exact:!0,path:"/about",component:H}),Object(n.jsx)(m.b,{path:"/",component:W})]}),Object(n.jsx)(J,{})]})})}}]),a}(s.Component),Y=function(e){e&&e instanceof Function&&a.e(3).then(a.bind(null,181)).then((function(t){var a=t.getCLS,n=t.getFID,s=t.getFCP,c=t.getLCP,i=t.getTTFB;a(e),n(e),s(e),c(e),i(e)}))};i.a.render(Object(n.jsx)(r.a,{children:Object(n.jsx)(z,{})}),document.getElementById("root")),Y()},50:function(e,t,a){},64:function(e,t,a){},79:function(e,t,a){},93:function(e,t,a){}},[[176,1,2]]]);
//# sourceMappingURL=main.ce0e2582.chunk.js.map