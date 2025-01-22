import PropTypes from "prop-types";
import useIsAdmin from "../hooks/useIsAdmin";

const AdminRoute = ({ username, children }) => {
  const { isAdmin, loading, error } = useIsAdmin(username);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return isAdmin ? children : <div>Unauthorized</div>;
};

AdminRoute.propTypes = {
  username: PropTypes.string.isRequired,
  children: PropTypes.node,
};

export default AdminRoute;
