import React, { useEffect } from "react";

function NotificationDetail(props) {
  useEffect(() => {
    console.log(props.match.params.id)
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <>
    <div>
        {props.match.params.id}
    </div>
    </>
  );
}

export default NotificationDetail;
